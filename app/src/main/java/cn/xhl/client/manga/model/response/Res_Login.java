package cn.xhl.client.manga.model.response;

import java.io.Serializable;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 */

public class Res_Login implements Serializable{
    private String access_token;
    private String expire_time;
    private String uid;
    private String salt;

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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

    public Res_Login(String access_token, String expire_time, String uid, String salt){
        super();
        this.access_token = access_token;
        this.expire_time = expire_time;
        this.uid = uid;
        this.salt = salt;
    }

    public String toString() {
        return "Res_Login [access_token = " + access_token + ", expire_time = " + expire_time + ", uid = " + uid + ", salt = " + salt + "]";
    }
}
