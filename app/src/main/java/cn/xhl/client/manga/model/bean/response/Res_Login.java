package cn.xhl.client.manga.model.bean.response;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 */

public class Res_Login {
    private String token;
    private String expire_time;
    private String uid;
    private String salt;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpire_time() {
        return this.expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Res_Login() {}

    public Res_Login(String token, String expire_time, String uid, String salt){
        super();
        this.token = token;
        this.expire_time = expire_time;
        this.uid = uid;
        this.salt = salt;
    }

    public String toString() {
        return "Res_Login [token = " + token + ", expire_time = " + expire_time + ", uid = " + uid + ", salt = " + salt + "]";
    }
}
