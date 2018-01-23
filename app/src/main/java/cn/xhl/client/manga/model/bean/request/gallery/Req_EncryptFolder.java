package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * Created by xiuhaoli on 2017/12/5.
 */
public class Req_EncryptFolder {
    private String folder;

    public Req_EncryptFolder() {
    }

    public Req_EncryptFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "Req_DeleteFolder{" +
                "folder='" + folder + '\'' +
                '}';
    }
}
