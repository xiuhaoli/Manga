package cn.xhl.client.manga.model.bean.response.gallery;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */

public class Res_BrowseImage {
    private String n;
    private String i3;

    public Res_BrowseImage(String n, String i3) {
        this.n = n;
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

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "Res_BrowseImage{" +
                "n='" + n + '\'' +
                ", i3='" + i3 + '\'' +
                '}';
    }
}
