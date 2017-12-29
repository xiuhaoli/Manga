package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/21
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_CommentList {
    /**
     * 页码
     */
    private int page;
    /**
     * 每页的长度
     */
    private int size;

    private int galleryId;

    public Req_CommentList(int page, int size, int galleryId) {
        this.page = page;
        this.size = size;
        this.galleryId = galleryId;
    }

    public Req_CommentList() {
    }

    public int getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(int galleryId) {
        this.galleryId = galleryId;
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
        return "Req_CommentList{" +
                "page=" + page +
                "galleryId=" + galleryId +
                ", size=" + size +
                '}';
    }
}
