package com.auc.common.constants;

/**
 *
 * 常量
 */
public final class AucConst {

  /**
   * 数据库里默认的version值
   */
  public static final Integer DEFAULT_VERSION = 0;

  /**
   * 正则：手机号（精确）
   * <p>
   * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184
   * 、187、188、198
   * </p>
   * <p>
   * 联通：130、131、132、145、155、156、175、176、185、186、166
   * </p>
   * <p>
   * 电信：133、153、173、177、180、181、189、199
   * </p>
   * <p>
   * 全球星：1349
   * </p>
   * <p>
   * 虚拟运营商：170
   * </p>
   */
  public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
  /**
   * 正则： 身份证格式是否正确
   */
  public static final String REGEX_ID_CARD = "^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$";
  /**
   * 正则: 邮箱格式
   */
  public static final String REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
  /**
   * 正则：固定电话格式
   */
  public static final String REGEX_OFFICE_PHONE = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";

  /**
   * header jwt
   */
  public static final String JWT_AUTH_TOKEN_KEY_NAME = "aucJWTToken";

  /**
   * token密钥的redis路径头
   */
  public static final String JWT_SECRET_REDIS_PATH = "auc:jwt:secret:";

  /**
   * userId对应的token redis路径头
   */
  public static final String JWT_USER_REDIS_PATH = "auc:jwt:user:";

  /**
   * REDIS 密码错误路径头
   */
  public static final String PW_ERROR_REDIS_PATH = "auc:pw:error:";


  public static final String RU_VARIABLE_USER_SYNC = "user_sync";
  public static final String RU_VARIABLE_DS_SYNC = "ds_sync";
  public static final String RU_VARIABLE_CLEAR_LOG = "clear_log";
  public static final String DOMAIN_ACCOUNT ="domain";

  public static final String CLIENT_LOGIN_TYPE_LIMIT="{\"SYSTEM\":[],\"MOBILE\":[],\"DOMAIN\":[{\"code\":\"GOMEDQ\", \"name\":\"国美电器\"}, {\"code\":\"GOMEKG\", \"name\":\"国美控股\"}]}";
}
