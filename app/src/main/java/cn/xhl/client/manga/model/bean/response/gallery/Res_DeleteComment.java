package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/21
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Res_DeleteComment {
    private String msg;

    public Res_DeleteComment(String msg) {
        this.msg = msg;
    }

    public Res_DeleteComment() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Res_DeleteComment{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
