package cn.xhl.client.manga.model.bean.response;

import java.util.List;

/**
 * @author Mike on 2017/10/23 0023.
 */
public class Res_GalleryList {
    private List<GalleryEntity> data;
    private boolean loadMore;

    public Res_GalleryList(List<GalleryEntity> data, boolean loadMore) {
        this.data = data;
        this.loadMore = loadMore;
    }

    public Res_GalleryList() {
    }

    public List<GalleryEntity> getData() {
        return data;
    }

    public void setData(List<GalleryEntity> data) {
        this.data = data;
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public String toString() {
        return "Res_GalleryList{" +
                "data=" + data +
                ", loadMore=" + loadMore +
                '}';
    }

    public static class GalleryEntity {
        private int id;
        private int gid;
        private String token;
        private String title;
        private String category;
        private String thumb;
        private String uploader;
        private int posted;
        private int filecount;
        private String rating;
        private String artist;
        private int subscribe;
        private int viewed;
        private String language;
        private int pagetoken;

        public GalleryEntity(int id, int gid, String token, String title, String category, String thumb,
                             String uploader, int posted, int filecount, String rating, String artist,
                             int subscribe, int viewed, String language, int pagetoken) {
            this.id = id;
            this.gid = gid;
            this.token = token;
            this.title = title;
            this.category = category;
            this.thumb = thumb;
            this.uploader = uploader;
            this.posted = posted;
            this.filecount = filecount;
            this.rating = rating;
            this.artist = artist;
            this.subscribe = subscribe;
            this.viewed = viewed;
            this.language = language;
            this.pagetoken = pagetoken;
        }

        public GalleryEntity() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getUploader() {
            return uploader;
        }

        public void setUploader(String uploader) {
            this.uploader = uploader;
        }

        public int getPosted() {
            return posted;
        }

        public void setPosted(int posted) {
            this.posted = posted;
        }

        public int getFilecount() {
            return filecount;
        }

        public void setFilecount(int filecount) {
            this.filecount = filecount;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public int getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(int subscribe) {
            this.subscribe = subscribe;
        }

        public int getViewed() {
            return viewed;
        }

        public void setViewed(int viewed) {
            this.viewed = viewed;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public int getPagetoken() {
            return pagetoken;
        }

        public void setPagetoken(int pagetoken) {
            this.pagetoken = pagetoken;
        }

        @Override
        public String toString() {
            return "GalleryEntity{" +
                    "id=" + id +
                    ", gid=" + gid +
                    ", token='" + token + '\'' +
                    ", title='" + title + '\'' +
                    ", category='" + category + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", uploader='" + uploader + '\'' +
                    ", posted=" + posted +
                    ", filecount=" + filecount +
                    ", rating='" + rating + '\'' +
                    ", artist='" + artist + '\'' +
                    ", subscribe=" + subscribe +
                    ", viewed=" + viewed +
                    ", language='" + language + '\'' +
                    ", pagetoken=" + pagetoken +
                    '}';
        }
    }
}