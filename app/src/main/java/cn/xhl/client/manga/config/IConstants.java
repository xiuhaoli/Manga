package cn.xhl.client.manga.config;

/**
 * Created by lixiuhao on 2017/9/21 0021.
 */

public interface IConstants {
    String USER_INFO = "USER_INFO";// 用于SharePreference保存用户信息
    long CACHE_MAX_SIZE = 10 * 1024 * 1024;// 缓存最大容量
    long CONNECT_TIMEOUT = 10 * 1000;// 连接超时时间(毫秒)
    long READ_TIMEOUT = 10 * 1000;// 读取超时时间
}
