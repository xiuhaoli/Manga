package cn.xhl.client.manga.model.bean.request.user;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2018/1/16
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Req_Attention {
    private String follow;

    public Req_Attention() {
    }

    public Req_Attention(String follow) {
        this.follow = follow;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    @Override
    public String toString() {
        return "Req_Attention{" +
                "follow='" + follow + '\'' +
                '}';
    }
}
