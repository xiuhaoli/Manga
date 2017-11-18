package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * @author lixiuhao on 2017/11/1 0001.
 */
public class Req_Subscribe {
    private int id;
    private String folder;

    public Req_Subscribe() {
    }

    public Req_Subscribe(int id, String folder) {
        this.id = id;
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "Req_Subscribe{" +
                "id=" + id +
                ", folder='" + folder + '\'' +
                '}';
    }
}
