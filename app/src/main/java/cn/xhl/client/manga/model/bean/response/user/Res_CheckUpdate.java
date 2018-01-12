package cn.xhl.client.manga.model.bean.response.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/9
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Res_CheckUpdate {
    private boolean install;
    private String version_name;
    private int version_code;
    private String log;
    private String size;
    private String hash;
    private String url;

    public Res_CheckUpdate() {
    }

    public Res_CheckUpdate(boolean install) {
        this.install = install;
    }

    public Res_CheckUpdate(boolean install, String version_name, int version_code,
                           String log, String size, String hash, String url) {
        this.install = install;
        this.version_name = version_name;
        this.version_code = version_code;
        this.log = log;
        this.size = size;
        this.hash = hash;
        this.url = url;
    }

    public boolean isInstall() {
        return install;
    }

    public void setInstall(boolean install) {
        this.install = install;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    @Override
    public String toString() {
        return "Res_CheckUpdate{" +
                "install=" + install +
                ", version_name='" + version_name + '\'' +
                ", version_code=" + version_code +
                ", log='" + log + '\'' +
                ", size='" + size + '\'' +
                ", hash='" + hash + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
