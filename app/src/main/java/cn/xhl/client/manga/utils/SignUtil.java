package cn.xhl.client.manga.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.model.bean.request.BaseRequest;

/**
 * @author Mike on 2017/9/28 0028.
 * <p>
 * 签名工具类
 */
public class SignUtil {
    private static UserInfo userInfo = UserInfo.getInstance();

    /**
     * 获取URL签名，按照字典序进行加签
     * sign = md5(md5(salt + 字典排序的参数value + salt) + 固定salt)
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
        return MD5Util.encrypt(MD5Util.encrypt(salt + stringBuilder.toString() + salt) + IConstants.SALT);
    }
}
