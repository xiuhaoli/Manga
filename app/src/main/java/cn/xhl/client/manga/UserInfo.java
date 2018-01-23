package cn.xhl.client.manga;



/**
 * @author Mike on 2017/4/19 0019.
 *         <p>
 *         用户信息的管理类
 *         单例，饿汉模式
 */
public class UserInfo {
    private static UserInfo userInfo = new UserInfo();

    private String profile_picture;// 头像的URL
    private String username;
    private String token;
    private String salt;
    private int uid;
    private String email;
    private boolean isNightMode;
    private boolean isNonhMode;
    private String categoryMode;//(All,Non-H)
    private String filter;

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        return userInfo;
    }

    public String getCategoryMode() {
        return categoryMode;
    }

    public void setCategoryMode(String categoryMode) {
        this.categoryMode = categoryMode;
    }

    public boolean isNonhMode() {
        return isNonhMode;
    }

    public void setNonhMode(boolean nonhMode) {
        isNonhMode = nonhMode;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
