package com.auc.web.aspect;

import cn.hutool.http.useragent.UserAgent;
import com.auc.common.enums.LogTypeEnum;
import com.auc.common.log.RequestMethodInfo;
import com.auc.common.utils.IpUtils;
import com.auc.common.utils.JsonUtil;
import com.auc.common.utils.SnowFlake;
import com.auc.dao.Log;
import com.auc.service.ILogService;
import com.auc.web.help.ContextUtil;
import com.auc.web.security.JWTAuthenticationToken;
import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Component
@Aspect
public class HttpRequestLoggerAspect {

  @Autowired
  public ILogService logService;

  private static final Logger LOGGER = LoggerFactory.getLogger("HTTP_LOG");
  private UserAgentParser parser;

  @PostConstruct
  public void initUserAgentParser(){
    try {
      parser =
          new UserAgentService().loadParser(Arrays.asList(BrowsCapField.BROWSER, BrowsCapField.BROWSER_TYPE,
              BrowsCapField.BROWSER_MAJOR_VERSION,
              BrowsCapField.DEVICE_TYPE, BrowsCapField.PLATFORM, BrowsCapField.PLATFORM_VERSION,
              BrowsCapField.RENDERING_ENGINE_VERSION, BrowsCapField.RENDERING_ENGINE_NAME,
              BrowsCapField.PLATFORM_MAKER, BrowsCapField.RENDERING_ENGINE_MAKER));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }



  @Around("@annotation(requestMapping)")
  public Object disPlayRequestInfo(ProceedingJoinPoint point, RequestMapping requestMapping) throws Throwable {
    return doLog(point, LOGGER);
  }
  @Around("@annotation(postMapping)")
  public Object disPlayRequestInfo(ProceedingJoinPoint point, PostMapping postMapping) throws Throwable {
    return doLog(point, LOGGER);
  }
  @Around("@annotation(getMapping)")
  public Object disPlayRequestInfo(ProceedingJoinPoint point, GetMapping getMapping) throws Throwable {
    return doLog(point, LOGGER);
  }

  private static final String CRLF = "\r\n";

  private static final String PARAM = "PARAM:";

  private static final String RESULT = "RESULT:";

  private static final String ERROR = "ERROR:";


  protected Object doLog(ProceedingJoinPoint point, Logger logger) throws Throwable {
    String className = null;
    String methodName = null;
    Method method = null;
    String paramLog = null;
    long startTime = 0l;
    try {
      Signature signature = point.getSignature();
      Object[] args = point.getArgs();
      MethodSignature methodSignature = null;
      boolean hasResult = false;
      if (signature instanceof MethodSignature) {
        methodSignature = (MethodSignature) signature;
        method = methodSignature.getMethod();
        methodName = method.getName();
        className = method.getDeclaringClass().getName();
        hasResult = methodSignature.getReturnType() != Void.TYPE
            && methodSignature.getReturnType() != Void.class;
      } else {
        return point.proceed();
      }

      RequestMethodInfo requestInfo = getRequestInfo(methodSignature, args, logger);

      paramLog = getLogMessage(requestInfo);
      if (requestInfo.isDoInfoLog()) {
        logger.info(paramLog);
      }

      Object resultObject = null;
      startTime = System.currentTimeMillis();
      if (hasResult) {
        resultObject = point.proceed();
      } else {
        point.proceed();
      }

      long spendTime = System.currentTimeMillis()- startTime;
      this.inLog(this.getLogType(requestInfo.getRequestMapping()),
          paramLog.toString(),spendTime,null);//不打印返回对象
      return resultObject;
    } catch (Throwable e) {
      if (paramLog != null) {
        logger.error("执行如下操作失败，方法以及参数为：===============================");
        logger.error(paramLog);
      }
      logger.error("具体错误如下：：===============================");
      logger.error("========", e);
      this.inLog(LogTypeEnum.ERROR,paramLog,
          startTime == 0L ? 0L : System.currentTimeMillis()- startTime
          ,ExceptionUtils.getStackTrace(e));
      throw e;
    }
  }

  private LogTypeEnum getLogType(String getRequestMapping){
    return LogTypeEnum.valueOfPath(getRequestMapping);
  }

  @Async("logTaskExecutor")
  public void inLog(LogTypeEnum lt,String paramLog,long spendTime,Object resultObject) throws JsonProcessingException {
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    Log log = new Log();
    log.setId(new SnowFlake().nextId());
    log.setIp(IpUtils.getIpAddr(request));
    JWTAuthenticationToken jt =ContextUtil.getJWTAuthenticationNoException();
    if(jt!=null){
      log.setUserName(jt.getUserName());
      log.setUserId(jt.getUserId());
    }
    log.setContent(new StringBuilder(PARAM).append(CRLF).append(paramLog == null ? "" : paramLog)
            .append(CRLF).append(lt.getCode() == 1 ? ERROR : RESULT).append(CRLF)
        .append(resultObject == null ? "" : resultObject).toString());
    log.setDevice(this.getAgentInfo(request));
    log.setLogType(lt.getCode());
    log.setSpendTime(spendTime);
    logService.asyncInLog(log);
  }

  private String getAgentInfo(HttpServletRequest request){
    String agent=request.getHeader("User-Agent");
    //解析agent字符串
    Capabilities capabilities = parser.parse(agent);
    StringBuilder sb = new StringBuilder();
    //sb.append("agent:"+agent).append(CRLF);
    sb.append("浏览器名:"+capabilities.getBrowser()).append(CRLF);
    //sb.append("浏览器类型:"+browser.getBrowserType()).append(CRLF);
    //sb.append("浏览器家族:"+browser.getGroup()).append(CRLF);
    //sb.append("浏览器生产厂商:"+browser.getManufacturer()).append(CRLF);
    //sb.append("浏览器使用的渲染引擎:"+browser.getRenderingEngine()).append(CRLF);
    sb.append("浏览器版本:"+capabilities.getBrowserMajorVersion()).append(CRLF);
    sb.append("操作系统名:"+capabilities.getPlatform()).append(CRLF);
    sb.append("访问设备类型:"+capabilities.getDeviceType()).append(CRLF);
    //sb.append("操作系统家族:"+operatingSystem.getGroup()).append(CRLF);
    //sb.append("操作系统生产厂商:"+operatingSystem.getManufacturer());
    return sb.toString();
  }

  private String getLogMessage(RequestMethodInfo requestInfo) {
    StringBuilder sb = new StringBuilder();
    if (requestInfo != null) {
      sb.append(
          "method:" + requestInfo.getClassName() + "." + requestInfo.getMethodString() + CRLF);
      if (requestInfo.getRequestMapping() != null) {
        sb.append("  requestMapping:" + requestInfo.getRequestMapping() + CRLF);
      }
      Map<String, String> otherParams = requestInfo.getOtherParams();
      if (otherParams != null) {
        sb.append("  params =============================================" + CRLF);
        for (Map.Entry<String, String> paramEntry : otherParams.entrySet()) {
          sb.append("  key: [" + paramEntry.getKey() + "], value :[" + paramEntry.getValue() + "]"
              + CRLF);
        }
      }
      Map<String, String> requestParams = requestInfo.getRequestParams();
      if (requestParams != null) {
        sb.append(" httpRequestParams ====================================" + CRLF);
        for (Map.Entry<String, String> paramEntry : requestParams.entrySet()) {
          sb.append("  key: [" + paramEntry.getKey() + "], value :[" + paramEntry.getValue() + "]"
              + CRLF);
        }
      }
      Map<String, String> requestHeaderParams = requestInfo.getRequestHeaderParams();
      if (requestHeaderParams != null) {
        sb.append(" requestHeaderParams ====================================" + CRLF);
        for (Map.Entry<String, String> paramEntry : requestHeaderParams.entrySet()) {
          sb.append("  key: [" + paramEntry.getKey() + "], value :[" + paramEntry.getValue() + "]"
              + CRLF);
        }
      }
    }
    return sb.toString();
  }

  private RequestMethodInfo getRequestInfo(MethodSignature methodSignature, Object[] args,
      Logger logger) {
    RequestMethodInfo requestInfo = new RequestMethodInfo();
    Class<?>[] parameterTypes = methodSignature.getParameterTypes();
    Method method = methodSignature.getMethod();
    String className = method.getDeclaringClass().getName();
    String methodName = method.getName();
    Class<?> clazz = method.getDeclaringClass();
    RequestMapping clazzRequestMapping = clazz.getAnnotation(RequestMapping.class);
    StringBuilder requestMappingStr = new StringBuilder();
    if (clazzRequestMapping != null) {
      requestMappingStr.append(Joiner.on("|").join(clazzRequestMapping.value()));
    }
    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
    if (requestMapping != null && requestMapping.value() != null) {
      String[] mappingStrArray = requestMapping.value();
      for (String mapping : mappingStrArray) {
        if (mapping != null) {
          mapping = mapping.startsWith("/") ? mapping : "/" + mapping;
          requestMappingStr.append(mapping);
        }
      }
      requestInfo.setRequestMapping(requestMappingStr.toString());
    }
    requestInfo.setClassName(className);
    requestInfo.setMethodName(methodName);
    LocalVariableTableParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    String[] paramNames = paramNameDiscoverer.getParameterNames(methodSignature.getMethod());
    try {
      StringBuilder methodSb = new StringBuilder();
      methodSb.append(methodName + "(");
      if (parameterTypes != null && parameterTypes.length > 0) {
        Map<String, String> paramMap = Maps.newHashMap();
        int paramLength = parameterTypes.length;
        for (int i = 0; i < paramLength; i++) {
          Class<?> type = parameterTypes[i];
          Object arg = args[i];
          if (arg == null) {
            continue;
          }
          String paramName = null;
          if (paramNames != null) {
            paramName = paramNames[i];
          }
          if (paramName == null) {
            paramName = "arg" + i;
          }
          methodSb.append(type.getSimpleName() + " " + paramName);
          if (i < paramLength - 1) {
            methodSb.append(", ");
          }
          Package argPackage = arg.getClass().getPackage();
          String value = null;
          if (arg instanceof HttpServletRequest) {
            Map<String, String> requestParamMap = Maps.newHashMap();
            HttpServletRequest request = (HttpServletRequest) arg;
            Enumeration<String> requestParamNames = request.getParameterNames();
            requestInfo.setRequestParams(requestParamMap);
            while (requestParamNames.hasMoreElements()) {
              String requestParamName = requestParamNames.nextElement();
              String requestParamValue = request.getParameter(requestParamName);
              requestParamMap.put(requestParamName, requestParamValue);
            }
            Map<String, String> requestHeaderNamesMap = Maps.newHashMap();
            Enumeration<String> requestHeaderNames = request.getHeaderNames();
            requestInfo.setRequestHeaderParams(requestHeaderNamesMap);
            while (requestHeaderNames.hasMoreElements()) {
              String requestHeadName = requestHeaderNames.nextElement();
              String requestHeadValue = request.getHeader(requestHeadName);
              requestHeaderNamesMap.put(requestHeadName, requestHeadValue);
            }
          } else if (arg instanceof HttpServletResponse) {
            continue;
          } else if (arg instanceof String) {
            value = (String) arg;
          } else if (arg instanceof MultipartFile) {
            continue;
          } else if (argPackage != null && argPackage.getName().contains("org.springframework")) {
            continue;
          } else {
            value = JsonUtil.toStr(arg);
          }
          paramMap.put(paramName, value);
          requestInfo.setOtherParams(paramMap);
        }
      }
      methodSb.append(")");
      requestInfo.setMethodString(methodSb.toString());
      return requestInfo;
    } catch (Throwable t) {
      throw new RuntimeException(
          "logger " + className + "." + methodName + ";args:" + Arrays.toString(args) + "error", t);
    }
  }
}