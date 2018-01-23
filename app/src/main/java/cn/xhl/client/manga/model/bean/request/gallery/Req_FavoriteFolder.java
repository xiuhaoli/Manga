package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class Req_FavoriteFolder {
    private int page;
    private int size;
    private int id;// 被点击收藏的书籍id（在请求收藏列表时是带0上来）


    public Req_FavoriteFolder() {
    }

    public Req_FavoriteFolder(int page, int size, int id) {
        this.page = page;
        this.size = size;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Req_FavoriteFolder{" +
                "page=" + page +
                ", size=" + size +
                ", id=" + id +
                '}';
    }
}
