package cn.xhl.client.manga.config;

/**
 * @author Mike on 2017/4/29 0029.
 * <p>
 * 网络请求的返回码
 */
public interface HttpRespCode {
    long SUCCESS_CODE = 200L;
    long CHECK_SIGN_FAILED = 406L;
    long TOKEN_TIMEOUT = 407L;
    long TOKEN_ERR = 408L;
    long UID_ERR = 409L;
    long PAGE_UNAVAILABLE = 410L;
    long HAVE_BEEN_REGISTERED = 411L;
    long ACCOUNT_ERR = 412L;
    long PASSWORD_ERR = 413L;
    long HYPELINK_ERR = 416L;
    long VERIFY_CODE_ERR = 418L;
    long SERVER_ERR = 500L;
    long REGISTER_FAILED = 511L;
    long RESET_FAILED = 514L;
    long OBTAIN_DATA_FAILED = 516L;
    long REFRESH_TOKEN_FAILED = 521L;
    long REFRESH_TOKEN_TIMEOUT_FAILED = 522L;
    long REFRESH_SALT_FAILED = 523L;
    long OBTAIN_VERIFY_CODE_FAILED = 524L;

}
