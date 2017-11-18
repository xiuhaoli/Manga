package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * @author lixiuhao on 2017/11/2 0002.
 */
public class Req_Viewed {
    private int id;

    public Req_Viewed() {
    }

    public Req_Viewed(int id) {
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
