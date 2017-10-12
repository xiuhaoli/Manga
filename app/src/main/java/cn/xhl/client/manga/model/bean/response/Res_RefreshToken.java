package cn.xhl.client.manga.model.bean.response;

/**
 * Created by lixiuhao on 2017/9/28 0028.
 */

public class Res_RefreshToken {
    private String token;
    private String expire_time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    @Override
    public String toString() {
        return "Res_RefreshToken[token = " + token + ", expire_time = " + expire_time + "]";
    }
}
