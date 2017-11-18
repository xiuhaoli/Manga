package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */

public class Res_BrowseImage {
    private String i3;

    public Res_BrowseImage(String i3) {
        this.i3 = i3;
    }

    public Res_BrowseImage() {
    }

    public String getI3() {
        return i3;
    }

    public void setI3(String i3) {
        this.i3 = i3;
    }

    @Override
    public String toString() {
        return "Res_BrowseImage{" +
                ", i3='" + i3 + '\'' +
                '}';
    }
}
