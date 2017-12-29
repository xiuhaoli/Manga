package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/21
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_DeleteComment {
    private int id;
    private int galleryId;

    public Req_DeleteComment(int id, int galleryId) {
        this.id = id;
        this.galleryId = galleryId;
    }

    public Req_DeleteComment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(int galleryId) {
        this.galleryId = galleryId;
    }

    @Override
    public String toString() {
        return "Req_DeleteComment{" +
                "id=" + id +
                ", galleryId=" + galleryId +
                '}';
    }
}
