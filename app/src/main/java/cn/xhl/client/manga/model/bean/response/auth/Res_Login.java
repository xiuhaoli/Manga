package cn.xhl.client.manga.model.bean.response.auth;

/**
 * @author Mike on 2017/9/18 0018.
 */

public class Res_Login {
    private String token;
    private int expire_time;
    private int uid;
    private String salt;
    private String profile_picture;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpire_time() {
        return this.expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Res_Login() {
    }

    public Res_Login(String token, int expire_time, int uid, String salt,
                         String profile_picture, String username) {
        this.token = token;
        this.expire_time = expire_time;
        this.uid = uid;
        this.salt = salt;
        this.profile_picture = profile_picture;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Res_LoginInfo{" +
                "token='" + token + '\'' +
                ", expire_time=" + expire_time +
                ", uid=" + uid +
                ", salt='" + salt + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
