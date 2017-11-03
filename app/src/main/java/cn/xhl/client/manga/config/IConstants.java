package cn.xhl.client.manga.config;

/**
 * 参数
 *
 * @author Mike on 2017/9/21 0021.
 */
public interface IConstants {
    String USER_INFO = "COMICS_USER_INFO";// 用于SharePreference保存用户信息
    long CACHE_MAX_SIZE = 10 * 1024 * 1024;// 缓存最大容量
    long CONNECT_TIMEOUT = 20 * 1000;// 连接超时时间(毫秒)
    long READ_TIMEOUT = 20 * 1000;// 读取超时时间
    String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    String POST_URL = "https://api.e-hentai.org/api.php";
    /**
     * 使用BCrypt加密需要使用的盐，这个盐是固定的
     */
    String PASSWORD_SALT = "$2a$10$4kLirifHUt6Civ0i.P/LoO";
    /**
     * 请求的书籍类型
     */
    String ALL = "All";
    String NON_H = "Non-H";
    String WESTERN = "Western";
    String ARTIST_CG_SETS = "Artist CG Sets";
    String DOUJINSHI = "Doujinshi";
    String MISC = "Misc";
    String MANGA = "Manga";
    String IMAGE_SETS = "Image Sets";
    String COSPLAY = "cosplay";
    String GAME_CG_SETS = "Game CG Sets";
    /**
     * 请求列表的类型
     * 排行
     */
    String RANKING = "ranking";
    /**
     * 请求列表的类型
     * 推荐
     */
    String RECOMMEND = "recommend";
    /**
     * 请求列表的类型
     * 最新
     */
    String LATEST = "latest";
    /**
     * 请求最新数据带category
     */
    String CATEGORY_LATEST = "category_latest";
    /**
     * 请求浏览历史列表
     */
    String HISTORY = "history";
    /**
     * 请求收藏列表
     */
    String FAVORITE = "favorite";
}
