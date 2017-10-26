package cn.xhl.client.manga.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.model.bean.request.BaseRequest;

/**
 * @author Mike on 2017/9/28 0028.
 * <p>
 * 签名工具类
 */
public class SignUtil {
    private static UserInfo userInfo = UserInfo.getInstance();

    /**
     * 获取网络请求的参数
     * 此方法只适用于只需要uid、token、timestamp、sign这四个参数的请求
     *
     * @return 公有签名
     */
    @Deprecated
    public static String getBaseSign() {
        Map<String, String> map = getBaseMap();
        return getSign(map);
    }

    /**
     * 获取书籍列表
     *
     * @param category 书籍类型
     * @return
     */
    @Deprecated
    public static String getComicListSign(String category) {
        Map<String, String> map = getBaseMap();
        map.put("category", category);
        return getSign(map);
    }

    /**
     * 获取参数所需的公共部分
     *
     * @return
     */
    @Deprecated
    private static Map<String, String> getBaseMap() {
        String token = userInfo.getToken();
        String uid = String.valueOf(userInfo.getUid());
        String timestamp = SystemUtil.getTimeStamp();

        Map<String, String> map = new TreeMap<>();// 这里使用TreeMap是为了让参数字典排序
        map.put("uid", uid);
        map.put("token", token);
        map.put("timestamp", timestamp);

        return map;
    }

    /**
     * 获取URL签名，按照字典序进行加签
     * sign = md5(salt + 字典排序的参数名 + salt)
     * salt在登录时获取并存于本地，只要salt不被知道，签名就会不过，
     * 因此在登录的时候，salt要采用三次握手的方式加密下发，
     * 这样可以确保，第三方拿到token等参数由于没有salt而无法过验签
     *
     * @param map 包含URL所需参数的表
     * @return 签名
     */
    @Deprecated
    private static String getSign(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        String sign = userInfo.getSalt();
        while (iterator.hasNext()) {
            sign += map.get(iterator.next());
            iterator.remove();
        }
        sign += userInfo.getSalt();
        return MD5Util.encrypt(sign);
    }

    /**
     * 获取URL签名，按照字典序进行加签
     * sign = md5(salt + 字典排序的参数value + salt)
     * salt在登录时获取并存于本地，只要salt不被知道，签名就会不过，
     * 因此在登录的时候，salt要采用三次握手的方式加密下发，
     * 这样可以确保，第三方拿到token等参数由于没有salt而无法过验签
     *
     * @param params 需要进行加签的参数
     * @return
     */
    public static String generateSign(String... params) {
        String salt = userInfo.getSalt();
        String[] p = new String[params.length + 3];
        p[0] = userInfo.getToken();
        p[1] = SystemUtil.getTimeStamp();
        p[2] = String.valueOf(userInfo.getUid());
        for (int i = 0, size = params.length; i < size; i++) {
            p[i + 3] = params[i];
        }
        params = StringUtil.bubbleSort(p);
        StringBuilder stringBuilder = new StringBuilder("");
        for (String param : params) {
            stringBuilder.append(param);
        }
        return MD5Util.encrypt(salt + stringBuilder.toString() + salt);
    }
}
