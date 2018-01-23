package cn.xhl.client.manga.model.bean.request.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/16
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_FollowExist {
    private String artist;
    private String uploader;

    public Req_FollowExist() {
    }

    public Req_FollowExist(String artist, String uploader) {
        this.artist = artist;
        this.uploader = uploader;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    @Override
    public String toString() {
        return "Req_FollowExist{" +
                "uploader='" + uploader + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
