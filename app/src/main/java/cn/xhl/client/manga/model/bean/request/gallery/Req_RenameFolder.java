package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class Req_RenameFolder {
    /**
     * 收藏夹
     */
    private String oldFolder;
    private String newFolder;

    public Req_RenameFolder() {
    }

    public Req_RenameFolder(String oldFolder, String newFolder) {
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
    }

    public String getOldFolder() {
        return oldFolder;
    }

    public void setOldFolder(String oldFolder) {
        this.oldFolder = oldFolder;
    }

    public String getNewFolder() {
        return newFolder;
    }

    public void setNewFolder(String newFolder) {
        this.newFolder = newFolder;
    }

    @Override
    public String toString() {
        return "Req_RenameFolder{" +
                "oldFolder='" + oldFolder + '\'' +
                ", newFolder='" + newFolder + '\'' +
                '}';
    }
}
