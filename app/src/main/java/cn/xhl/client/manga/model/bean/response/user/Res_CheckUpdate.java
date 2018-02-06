package cn.xhl.client.manga.model.bean.response.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/9
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Res_CheckUpdate {
    private String tag_name;
    private String name;
    private String body;

    public Res_CheckUpdate() {
    }

    public Res_CheckUpdate(String tag_name, String name, String body) {
        this.tag_name = tag_name;
        this.name = name;
        this.body = body;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Res_CheckUpdate{" +
                "tag_name='" + tag_name + '\'' +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
