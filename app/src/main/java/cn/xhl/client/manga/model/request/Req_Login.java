package cn.xhl.client.manga.model.request;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 */

public class Req_Login {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
