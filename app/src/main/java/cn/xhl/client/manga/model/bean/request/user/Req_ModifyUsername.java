package cn.xhl.client.manga.model.bean.request.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/23
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_ModifyUsername {
    private String username;

    public Req_ModifyUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Req_ModifyProfileHeader{" +
                "username='" + username + '\'' +
                '}';
    }
}
