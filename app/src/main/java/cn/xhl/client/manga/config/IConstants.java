package cn.xhl.client.manga.config;

/**
 * 参数
 *
 * @author Mike on 2017/9/21 0021.
 */
public interface IConstants {
    String USER_INFO = "COMICS_USER_INFO";// 用于SharePreference保存用户信息
    String NIGHT_MODE = "night_mode";// sp的key
    String NON_H_MODE = "non_h_mode";
    String FILTER = "filter";
    String IGNORE_APK_INSTALL = "ignore_apk_install";
    String USERNAME = "username";
    String PROFILE_HEADER = "profile_header";
    long CACHE_MAX_SIZE = 10 * 1024 * 1024;// 缓存最大容量
    long CONNECT_TIMEOUT = 20 * 1000;// 连接超时时间(毫秒)
    long READ_TIMEOUT = 20 * 1000;// 读取超时时间
    String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    String POST_URL = "https://api.e-hentai.org/api.php";
    String APK_URL = "https://api.github.com/repos/xiuhaoli/Manga/releases/latest";
    String APK_PRE_URL = "https://github.com/xiuhaoli/Manga/releases/download/";
    String FILE_PROVIDER = "cn.xhl.client.manga.fileprovider";

    /**
     * 这个盐是固定的
     */
    String SALT = "$2a$10$4kLirifHUt6Civ0i.P/LoO";
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
    String JAPANESE = "Japanese";
    String ENGLISH = "English";
    String CHINESE = "Chinese";
    String KOREAN = "Korean";
    String SPANISH = "Spanish";
    String RUSSIAN = "Russian";
    String VIETNAMESE = "Vietnamese";
    String FRENCH = "French";
    String PORTUGUESE = "Portuguese";
    String THAI = "Thai";
    String GERMAN = "German";
    String POLISH = "Polish";
    String ITALIAN = "Italian";
    String GREEK = "Greek";
    String DEFAULT_CATEGORY = IConstants.ALL;
    String[] ALL_LANGUAGE = {IConstants.JAPANESE,
            IConstants.ENGLISH, IConstants.CHINESE, IConstants.KOREAN, IConstants.SPANISH,
            IConstants.RUSSIAN, IConstants.VIETNAMESE, IConstants.FRENCH, IConstants.THAI,
            IConstants.PORTUGUESE, IConstants.GERMAN, IConstants.POLISH, IConstants.GREEK,
            IConstants.ITALIAN
    };
    String[] ALL_CATEGORY = {IConstants.NON_H, IConstants.DOUJINSHI, IConstants.ARTIST_CG_SETS,
            IConstants.COSPLAY, IConstants.GAME_CG_SETS, IConstants.IMAGE_SETS, IConstants.MANGA,
            IConstants.MISC, IConstants.WESTERN,
    };
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
     * 请求关注者信息
     */
    String ATTENTION = "attention";
    /**
     * 请求浏览历史列表
     */
    String HISTORY = "history";
    /**
     * 请求收藏列表
     */
    String FAVORITE = "favorite";
    /**
     * 请求其他用户的收藏列表
     */
    String OTHERS_FAVORITE = "others_favorite";
    /**
     * 搜索title类型列表
     */
    String TITLE = "title";
    /**
     * 搜索author类型列表
     */
    String AUTHOR = "author";
    /**
     * 搜索uploader类型列表
     */
    String UPLOADER = "uploader";
    /**
     * 语种的列表类型
     */
    String LANGUAGE = "language";
}
