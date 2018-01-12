package cn.xhl.client.manga.model.bean.request.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/9
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_CheckUpdate {
    private int version_code;
    private String version_name;

    public Req_CheckUpdate() {
    }

    public Req_CheckUpdate(int version_code, String version_name) {
        this.version_code = version_code;
        this.version_name = version_name;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }
}
