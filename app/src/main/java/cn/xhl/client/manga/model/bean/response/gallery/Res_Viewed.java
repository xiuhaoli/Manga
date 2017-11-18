package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * @author lixiuhao on 2017/11/2 0002.
 */
public class Res_Viewed {
    private String msg;

    public Res_Viewed(String msg) {
        this.msg = msg;
    }

    public Res_Viewed() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Res_Viewed{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
