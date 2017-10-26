package cn.xhl.client.manga.model.bean.response;

/**
 * @author Mike on 2017/9/29 0029.
 */

public class Res_ResetPassword {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Res_ResetPassword[msg = " + msg + "]";
    }
}
