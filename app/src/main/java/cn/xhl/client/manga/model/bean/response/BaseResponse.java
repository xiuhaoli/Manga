package cn.xhl.client.manga.model.bean.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Mike on 2017/9/18 0018.
 * <p>
 * 返回的json外层
 */
public class BaseResponse<E> {
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private E data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse[code = " + code + ", msg = " + msg + ", data = " + data + "]";
    }
}
