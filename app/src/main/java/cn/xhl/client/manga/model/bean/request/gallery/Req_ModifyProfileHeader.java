package cn.xhl.client.manga.model.bean.request.gallery;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/23
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_ModifyProfileHeader {
    private String url;

    public Req_ModifyProfileHeader(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Req_ModifyProfileHeader{" +
                "url='" + url + '\'' +
                '}';
    }
}
