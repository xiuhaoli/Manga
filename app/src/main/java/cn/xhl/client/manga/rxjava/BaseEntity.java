package cn.xhl.client.manga.rxjava;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 */

public class BaseEntity<E> {
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
}
