package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/18
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_FavoriteFolderOther {
    private int uid;
    private int page;
    private int size;

    public Req_FavoriteFolderOther() {
    }

    public Req_FavoriteFolderOther(int uid, int page, int size) {
        this.uid = uid;
        this.page = page;
        this.size = size;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Req_FavoriteFolderOther{" +
                "uid=" + uid +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
