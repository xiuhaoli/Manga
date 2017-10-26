package cn.xhl.client.manga.model.bean.request;

import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.utils.SystemUtil;

/**
 * 请求报文的json格式
 *
 * @author Mike
 */
public class BaseRequest {
    private String token;
    private int uid;
    private int timestamp;
    private String sign;
    private Object data;

    private BaseRequest(Builder builder) {
        this.token = builder.token;
        this.uid = builder.uid;
        this.timestamp = builder.timestamp;
        this.sign = builder.sign;
        this.data = builder.data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "token='" + token + '\'' +
                ", uid=" + uid +
                ", timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Builder {
        private String token = UserInfo.getInstance().getToken();
        private int uid = UserInfo.getInstance().getUid();
        private int timestamp = SystemUtil.getTimestamp();
        private String sign;
        private Object data;

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setUid(int uid) {
            this.uid = uid;
            return this;
        }

        public Builder setTimestamp(int timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public BaseRequest build() {
            if (StringUtil.isEmpty(sign)) {
                throw new IllegalArgumentException("签名不能为空");
            }
            return new BaseRequest(this);
        }
    }
}
