package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * @author lixiuhao on 2017/11/2 0002.
 */

public class Res_Subscribe {
    private int delta;
    private String msg;

    public Res_Subscribe(int delta, String msg) {
        this.delta = delta;
        this.msg = msg;
    }

    public Res_Subscribe() {
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Res_Subscribe{" +
                "delta=" + delta +
                ", msg='" + msg + '\'' +
                '}';
    }
}
