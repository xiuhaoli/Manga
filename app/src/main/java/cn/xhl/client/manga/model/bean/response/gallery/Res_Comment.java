package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * Created by lixiuhao on 2017/11/2 0002.
 */
public class Res_Comment {
    private String msg;
    private int id;
    private int create_time;

    public Res_Comment(String msg, int id,int create_time) {
        this.msg = msg;
        this.id = id;
        this.create_time = create_time;
    }

    public Res_Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Res_Comment{" +
                "id='" + id + '\'' +
                "msg='" + msg + '\'' +
                "create_time='" + create_time + '\'' +
                '}';
    }
}
