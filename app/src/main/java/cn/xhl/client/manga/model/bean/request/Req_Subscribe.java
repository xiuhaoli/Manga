package cn.xhl.client.manga.model.bean.request;

/**
 * @author lixiuhao on 2017/11/1 0001.
 */
public class Req_Subscribe {
    private int id;

    public Req_Subscribe() {
    }

    public Req_Subscribe(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Req_Subscribe{" +
                "id=" + id +
                '}';
    }
}
