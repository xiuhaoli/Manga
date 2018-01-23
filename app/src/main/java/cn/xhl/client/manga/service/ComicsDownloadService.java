package cn.xhl.client.manga.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.model.bean.request.gallery.Req_BrowseImage;
import cn.xhl.client.manga.model.bean.response.gallery.Res_BrowseImage;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载书籍的后台服务
 * 暂且不用
 *
 * Created on 2017/11/8.
 */
public class ComicsDownloadService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    private static final int TASK_LIMIT = 4;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);
    private static final SparseArray<Runnable> tasks = new SparseArray<>(TASK_LIMIT);
    private ComicsDownCallback comicsDownCallback;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        threadPool.shutdown();
        tasks.clear();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setComicsDownCallback(ComicsDownCallback comicsDownCallback) {
        this.comicsDownCallback = comicsDownCallback;
    }

    private class DownloadBinder extends Binder {
        private void startDownload(ComicsTask task) {
            threadPool.execute(task);
        }

        public void addDownloadTask(ComicsTask task) {
            if (tasks.size() >= TASK_LIMIT) {
                comicsDownCallback.onAddTaskFailure("already achieved the upper limit");
                return;
            }
            int key = task.getEntity().getId();
            if (tasks.get(key) != null) {
                comicsDownCallback.onAddTaskFailure("the task has been added");
                return;
            }
            tasks.put(key, task);
            startDownload(task);
        }
    }

    public interface ComicsDownCallback {
        void onProgress(int progress);

        void onFinish(String msg);

        void onFailure(String msg);

        void onAddTaskFailure(String msg);
    }

    public class ComicsTask implements Runnable {
        private Res_GalleryList.GalleryEntity entity;
        private String firstImg;
        private String showkey;
        private String secondImgkey;

        public ComicsTask(Res_GalleryList.GalleryEntity entity, String firstImg, String showkey, String secondImgkey) {
            this.entity = entity;
            this.firstImg = firstImg;
            this.showkey = showkey;
            this.secondImgkey = secondImgkey;
        }

        @Override
        public void run() {
            downloadImg(1, firstImg);
            reqImgUrl(2, secondImgkey);
        }

        private void reqImgUrl(final int page, String imgkey) {
            if (page > entity.getFilecount() || StringUtil.isEmpty(imgkey)) {
                return;
            }
            Req_BrowseImage req_browseImage = new Req_BrowseImage();
            req_browseImage.setGid(entity.getGid());
            req_browseImage.setImgkey(imgkey);
            req_browseImage.setPage(page);
            req_browseImage.setShowkey(showkey);
            MyApplication.getOkHttpClient().newCall(new Request.Builder()
                    .url(IConstants.POST_URL)
                    .post(StringUtil.getRequestBody(new Gson().toJson(req_browseImage)))
                    .build())
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(@Nullable Call call, @Nullable IOException e) {
                            // remove the task, it is over
                            tasks.remove(entity.getId());
                            comicsDownCallback.onFailure("download failed check your net");
                        }

                        @Override
                        public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                            ResponseBody body = null;
                            try {
                                if (response == null) {
                                    return;
                                }
                                body = response.body();
                                if (body == null) {
                                    return;
                                }
                                Res_BrowseImage res_browseImage = new Gson().fromJson(body.string(), Res_BrowseImage.class);
                                String imgkey;
                                if (page != entity.getFilecount()) {
                                    // 这是下一张图的imgkey
                                    imgkey = res_browseImage.getI3().split("'")[1].substring(0, 10);
                                    reqImgUrl(page + 1, imgkey);
                                }
                                // 这是当前的请求的图片URL
                                String url = res_browseImage.getI3().split("src")[1].split("\"")[1];
                                downloadImg(page, url);
                            } finally {
                                if (body != null) {
                                    body.close();
                                }
                            }
                        }
                    });
        }

        private void downloadImg(final int page, String url) {
            MyApplication.getOkHttpClient().newCall(new Request.Builder()
                    .url(url)
                    .get()
                    .build())
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(@Nullable Call call, @Nullable IOException e) {
                            // discard
                        }

                        @Override
                        public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                            ResponseBody body = null;
                            try {
                                if (response != null) {
                                    body = response.body();
                                    if (body != null) {
                                        FileUtil.getInstance().saveImgByte(body.bytes(), entity.getId() + File.separator + page);
                                        comicsDownCallback.onProgress((page / entity.getFilecount()) * 100);
                                    }
                                }
                                if (page == entity.getFilecount()) {
                                    comicsDownCallback.onFinish("download completed");
                                }
                            } finally {
                                if (body != null) {
                                    body.close();
                                }
                            }
                        }
                    });
        }

        public Res_GalleryList.GalleryEntity getEntity() {
            return entity;
        }

        public String getFirstImg() {
            return firstImg;
        }

        public String getShowkey() {
            return showkey;
        }

        public String getSecondImgkey() {
            return secondImgkey;
        }
    }
}
