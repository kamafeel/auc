package com.auc.common.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class FileUploadUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);

    private static final Environment environment = SpringContextUtil.getBean(Environment.class);

    /**
     * 默认大小 20M
     */
    private static long UPLOAD_MAX_SIZE = 20 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    private static final int DEFAULT_FILE_NAME_LENGTH = 100;


    private static GsonJsonParser gsonJsonParser = new GsonJsonParser();

    /**
     * GFS上传成功的返回标识
     */
    private static final String SUCCESS = "Y";

    /**
     * GFS上传图片的路径  默认的是测试环境地址，可在配置文件中配置
     */
    private static String IMAGE_UPLOAD_URL = "http://gfs.ds.gfsuat.com.cn/api/uploadImage";

    /**
     * GFS上传图片的token  默认的是测试环境token，可在配置文件中配置
     */
    private static String IMAGE_UPLOAD_TOKEN = "d350c4b43ec44fcdaf33a24e5128609b";

    /**
     * GFS上传图片外其他文件的路径  默认的是测试环境地址，可在配置文件中配置
     */
    private static String FILE_UPLOAD_URL = "http://gfs.ds.gfsuat.com.cn/api/uploadCommonFile?fileName=";

    /**
     * GFS上传文件的token  默认的是测试环境token，可在配置文件中配置
     */
    private static String FILE_UPLOAD_TOKEN = "d94fa4cf5efc4d37bf00b1fa18c2a019";

    static {
        if (logger.isInfoEnabled()) {
            logger.info("init uploader properties");
        }
        IMAGE_UPLOAD_URL = environment.containsProperty("gfs.image.url")
            ? environment.getProperty("gfs.image.url") : IMAGE_UPLOAD_URL;
        IMAGE_UPLOAD_TOKEN = environment.containsProperty("gfs.image.token")
            ? environment.getProperty("gfs.image.token") : IMAGE_UPLOAD_TOKEN;
        FILE_UPLOAD_URL = environment.containsProperty("gfs.file.url")
            ? environment.getProperty("gfs.file.url")+"?fileName=" : FILE_UPLOAD_URL;
        FILE_UPLOAD_TOKEN = environment.containsProperty("gfs.file.token")
            ? environment.getProperty("gfs.file.token") : FILE_UPLOAD_TOKEN;
        UPLOAD_MAX_SIZE = environment.containsProperty("gfs.upload.max-size")
            ? environment.getProperty("gfs.upload.max-size", Long.class) * 1024 * 1024
            : UPLOAD_MAX_SIZE;
        if (logger.isInfoEnabled()) {
            logger.info("GFS上传图片的路径：" + IMAGE_UPLOAD_URL +
                "\r\n GFS上传图片的token：" + IMAGE_UPLOAD_TOKEN +
                "\r\n GFS上传文件的路径：" + FILE_UPLOAD_URL +
                "\r\n GFS上传文件的token：" + FILE_UPLOAD_TOKEN);
        }
    }

    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static String upload(MultipartFile file) throws IOException {
        try {
            return upload(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return String 返回上传成功的文件路径
     * @throws RuntimeException 如果超出最大大小,文件名太长,文件校验异常
     * @throws IOException      比如读写文件出错时
     */
    public static String upload(MultipartFile file, String[] allowedExtension)
        throws RuntimeException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug(file.getOriginalFilename());
        }
        int fileNameLength = file.getOriginalFilename().length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new RuntimeException("upload.exceed.maxSize : " + FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        // 判断文件类型是否允许上传
        String extension = assertAllowed(file, allowedExtension);
        boolean isImageFile = Arrays.asList(MimeTypeUtils.IMAGE_EXTENSION).contains(extension);
        // 根据文件类型确定上传的url 和 token
        String token = isImageFile ? IMAGE_UPLOAD_TOKEN : FILE_UPLOAD_TOKEN;
        // 如果不是图片，文件明不能包含中文
        String url = isImageFile ? IMAGE_UPLOAD_URL : FILE_UPLOAD_URL + System.currentTimeMillis() + "." + extension;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.addHeader("token", token);
        post.setEntity(new ByteArrayEntity(file.getBytes()));
        HttpResponse httpResponse = httpClient.execute(post);
        String content = EntityUtils.toString(httpResponse.getEntity());
        Map<String, Object> result = gsonJsonParser.parseMap(content);
        if (logger.isDebugEnabled()) {
            logger.debug(result.toString());
        }
        if (SUCCESS.equals(result.get("result"))) {
            return (String) result.get("url");
        }
        throw new RuntimeException("upload failed: errorCode[" + result.get("errorCode") + "], msg[" + result.get("msg") + "]");
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return String 文件类型（允许上传才返回）
     * @throws RuntimeException 如果超出最大大小,文件校验异常
     */
    private static String assertAllowed(MultipartFile file, String[] allowedExtension)
        throws RuntimeException {
        long size = file.getSize();
        if (UPLOAD_MAX_SIZE != -1 && size > UPLOAD_MAX_SIZE) {
            throw new RuntimeException("upload.exceed.maxSize : " + UPLOAD_MAX_SIZE / 1024 / 1024 + "MB");
        }
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            throw new RuntimeException("filename : [" + fileName + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]");
        }
        return extension;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    private static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    private static String getExtension(MultipartFile file) {

        String extension = getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    private static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf(".");
        if (extensionPos == -1) {
            return null;
        }
        return filename.substring(extensionPos + 1);
    }

}
