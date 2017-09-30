package cn.xhl.client.manga.model.bean.response;

/**
 * Created by lixiuhao on 2017/9/29 0029.
 */

public class Res_Register {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Res_Register[msg = " + msg + "]";
    }
}
