package cn.xhl.client.manga;

/**
 * Created by lixiuhao on 2017/4/19 0019.
 * <p>
 * 用户信息的管理类
 * 单例，饿汉模式
 */
public class UserInfo {
    private static UserInfo userInfo = new UserInfo();

    private String url_avatar;// 头像的URL
    private String username;
    private String token;
    private String salt;
    private String uid;

    private UserInfo() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static UserInfo getInstance() {
        return userInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUrl_avatar() {
        return url_avatar;
    }

    public void setUrl_avatar(String url_avatar) {
        this.url_avatar = url_avatar;
    }

}
