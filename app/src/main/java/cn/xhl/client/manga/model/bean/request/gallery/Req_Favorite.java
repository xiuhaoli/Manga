package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class Req_Favorite {
    /**
     * 页码
     */
    private int page;
    /**
     * 每页的长度
     */
    private int size;

    /**
     * 收藏夹
     */
    private String folder;

    public Req_Favorite() {
    }

    public Req_Favorite(int page, int size, String folder) {
        this.page = page;
        this.size = size;
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
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
        return "Req_GalleryList{" +
                ", page=" + page +
                ", size=" + size +
                ", folder='" + folder + '\'' +
                '}';
    }
}
