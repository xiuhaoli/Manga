package cn.xhl.client.manga.utils;

/**
 * Created by lixiuhao on 2017/4/11 0011.
 */

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.xhl.client.manga.MyApplication;

public class FileUtil {
    private static final String TAG = "FileUtil";
    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = null;
    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/cn.client.manga";

    private final static String IMAGE = "/Image";

    private final static String LOG = "/log";

    private final static String DOWNLOAD = "/download";

    private final static String CACHE = "/cache";

    private static FileUtil fileUtil = new FileUtil();

    public static FileUtil getInstance() {
        return fileUtil;
    }

    public FileUtil() {
        mDataRootPath = MyApplication.getAppContext().getCacheDir().getPath();
        File imgPath = new File(getStorageDirectory() + IMAGE);
        File logPath = new File(getStorageDirectory() + LOG);
        File downloadPath = new File(getStorageDirectory() + DOWNLOAD);
        File cachePath = new File(getStorageDirectory() + CACHE);
        if (!imgPath.exists()) {
            if (!imgPath.mkdirs()) {
                Log.e(TAG, "create image dir fault");
            }
        }
        if (!logPath.exists()) {
            if (!logPath.mkdirs()) {
                Log.e(TAG, "create log dir fault");
            }
        }
        if (!downloadPath.exists()) {
            if (!downloadPath.mkdirs()) {
                Log.e(TAG, "create image dir fault");
            }
        }
        if (!cachePath.exists()) {
            if (!cachePath.mkdirs()) {
                Log.e(TAG, "create cache dir fault");
            }
        }
    }

    /**
     * 获取缓存应用根目录
     *
     * @return
     */
    public String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }

    /**
     * 获取log目录
     *
     * @return
     */
    public String getLogPath() {
        return getStorageDirectory() + LOG;
    }

    /**
     * 获取储存Image的目录
     *
     * @return
     */
    public String getImagePath() {
        return getStorageDirectory() + IMAGE;
    }

    /**
     * 获取下载文件的路径
     *
     * @return
     */
    public String getDownloadPath() {
        return getStorageDirectory() + DOWNLOAD;
    }

    /**
     * 获取缓存文件的路径
     *
     * @return
     */
    public String getCachePath() {
        return getStorageDirectory() + CACHE;
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public File savaBitmap(String fileName, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return null;
        }
        String path = getImagePath();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return file;
    }

    /**
     * 输出日志文件
     *
     * @param fileName 文件名
     * @param content  要输出的内容
     * @throws IOException
     */
    public void printLog(String fileName, String content) throws IOException {
        if (content == null) {
            return;
        }
        String path = getLogPath();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes("utf-8"));
        fos.flush();
        fos.close();
    }

    public void printLog(String content) throws IOException {
        printLog(String.valueOf(System.currentTimeMillis()), content);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     *
     * @param fileName
     * @return
     */
    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }


    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public void deleteFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                // 如果判断为缓存文件就不删除
                if (!file.getName().equals("journal")) {
                    if (!file.delete()) {
                        Log.e(TAG, "delete file fault");
                    }
                }
            } else if (file.isDirectory()) {
                deleteFile(file); // 递规的方式删除文件夹
            }
        }
//        不删除目录和缓存的日志文件，不然会出现清了缓存后，图片不能加载
//        dir.delete();// 删除目录本身
    }

    /**
     * 获取目录下所有文件的总大小
     *
     * @param dir  目录文件
     * @param size 初始大小(传0进来)
     * @return
     */
    public double getDirectorySize(File dir, double size) {
        if (dir == null || !dir.exists()) {
            return 0;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            } else if (file.isDirectory()) {
                size = getDirectorySize(file, size);
            }
        }
        return size;
    }
}
