package cn.xhl.client.manga.model.bean.response.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/16
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Res_FollowExist {
    private boolean artist;
    private boolean uploader;

    public Res_FollowExist() {
    }

    public Res_FollowExist(boolean artist, boolean uploader) {
        this.artist = artist;
        this.uploader = uploader;
    }

    public boolean isArtist() {
        return artist;
    }

    public void setArtist(boolean artist) {
        this.artist = artist;
    }

    public boolean isUploader() {
        return uploader;
    }

    public void setUploader(boolean uploader) {
        this.uploader = uploader;
    }

    @Override
    public String toString() {
        return "Res_FollowExist{" +
                "artist=" + artist +
                ", uploader=" + uploader +
                '}';
    }
}
