package cn.xhl.client.manga.model.bean.response;

/**
 * @author Mike on 2017/9/18 0018.
 */

public class Res_Login {
    private String token;
    private int expire_time;
    private int uid;
    private String salt;

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

    public Res_Login() {}

    public Res_Login(String token, int expire_time, int uid, String salt){
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
