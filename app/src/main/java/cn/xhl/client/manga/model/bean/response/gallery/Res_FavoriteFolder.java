package cn.xhl.client.manga.model.bean.response.gallery;

import java.util.List;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class Res_FavoriteFolder {
    private List<Data> folders;

    public Res_FavoriteFolder() {
    }

    public Res_FavoriteFolder(List<Data> folders) {
        this.folders = folders;
    }

    public List<Data> getFolders() {
        return folders;
    }

    @Override
    public String toString() {
        return "Res_FavoriteFolder{" +
                "folders=" + folders +
                '}';
    }

    public void setFolders(List<Data> folders) {
        this.folders = folders;
    }

    public static class Data {
        private String folder;
        private int count;
        private boolean checked;

        public Data() {
        }

        public Data(String folder, int count, boolean checked) {
            this.folder = folder;
            this.count = count;
            this.checked = checked;
        }

        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "folder='" + folder + '\'' +
                    ", count=" + count +
                    ", checked=" + checked +
                    '}';
        }
    }
}
