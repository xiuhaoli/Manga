package cn.xhl.client.manga.model.bean.response;

/**
 * @author Mike on 2017/9/28 0028.
 */

public class Res_RefreshToken {
    private String token;
    private int expire_time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    @Override
    public String toString() {
        return "Res_RefreshToken[token = " + token + ", expire_time = " + expire_time + "]";
    }
}
