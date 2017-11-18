package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */

public class Req_BrowseImage {
    private String method = "showpage";
    private int gid;
    private int page;
    private String imgkey;
    private String showkey;

    public Req_BrowseImage() {
    }

    public Req_BrowseImage(String method, int gid, int page, String imgkey, String showkey) {
        this.method = method;
        this.gid = gid;
        this.page = page;
        this.imgkey = imgkey;
        this.showkey = showkey;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getImgkey() {
        return imgkey;
    }

    public void setImgkey(String imgkey) {
        this.imgkey = imgkey;
    }

    public String getShowkey() {
        return showkey;
    }

    public void setShowkey(String showkey) {
        this.showkey = showkey;
    }

    @Override
    public String toString() {
        return "Req_BrowseImage{" +
                "method='" + method + '\'' +
                ", gid=" + gid +
                ", page=" + page +
                ", imgkey='" + imgkey + '\'' +
                ", showkey='" + showkey + '\'' +
                '}';
    }
}
