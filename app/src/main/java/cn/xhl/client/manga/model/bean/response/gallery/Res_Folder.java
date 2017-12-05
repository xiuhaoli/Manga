package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * Created by xiuhaoli on 2017/12/5.
 */

public class Res_Folder {
    private String msg;

    public Res_Folder() {
    }

    public Res_Folder(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Res_Folder{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
