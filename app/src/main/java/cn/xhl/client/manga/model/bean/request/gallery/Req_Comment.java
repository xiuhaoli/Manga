package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/21
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_Comment {
    private int galleryId;
    private String content;

    public Req_Comment(int galleryId, String content) {
        this.galleryId = galleryId;
        this.content = content;
    }

    public Req_Comment() {
    }


    public int getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(int galleryId) {
        this.galleryId = galleryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Req_Comment{" +
                ", galleryId=" + galleryId +
                ", content='" + content + '\'' +
                '}';
    }
}
