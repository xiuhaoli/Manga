package cn.xhl.client.manga.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.xhl.client.manga.UserInfo;

/**
 * Created by lixiuhao on 2017/9/28 0028.
 */

public class SignUtil {
    private static UserInfo userInfo = UserInfo.getInstance();

    /**
     * 获取网络请求的参数
     * 此方法只适用于只需要uid、token、timestamp、sign这四个参数的请求
     *
     * @return 公有签名
     */
    public static String getCommonSign() {
        Map<String, String> map = getCommonMap();
        return getSign(map);
    }

    /**
     * 获取参数所需的公共部分
     *
     * @return
     */
    private static Map<String, String> getCommonMap() {
        String token = userInfo.getToken();
        String uid = userInfo.getUid();
        String timestamp = SystemUtil.getTimeStamp();

        Map<String, String> map = new TreeMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("timestamp", timestamp);

        return map;
    }

    /**
     * 获取URL签名
     *
     * @param map 包含URL所需参数的表
     * @return
     */
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
}
