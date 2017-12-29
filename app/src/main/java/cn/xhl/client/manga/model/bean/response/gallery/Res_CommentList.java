package cn.xhl.client.manga.model.bean.response.gallery;


import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *      author xiuhaoli
 *      time   2017/12/21
 *      e-mail 526193779@qq.com
 * </pre>
 */
public class Res_CommentList {
    private List<CommentEntity> data;

    public Res_CommentList(List<CommentEntity> data) {
        this.data = data;
    }

    public Res_CommentList() {
    }

    public List<CommentEntity> getData() {
        return data;
    }

    public void setData(List<CommentEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Res_CommentList{" +
                "data=" + data +
                '}';
    }

    public static class CommentEntity implements Serializable{
        private int id;
        private int galleryId;
        private int from_uid;
        private String content;
        private String username;
        private String profile_picture;
        private int create_time;
        private int floor;// 当前评论的楼层(本地计算)

        public CommentEntity(int id, int galleryId, int from_uid, int create_time,int floor,
                             String content, String username, String profile_picture) {
            this.id = id;
            this.create_time = create_time;
            this.galleryId = galleryId;
            this.from_uid = from_uid;
            this.content = content;
            this.username = username;
            this.profile_picture = profile_picture;
            this.floor = floor;
        }

        public CommentEntity() {
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGalleryId() {
            return galleryId;
        }

        public void setGalleryId(int galleryId) {
            this.galleryId = galleryId;
        }

        public int getFrom_uid() {
            return from_uid;
        }

        public void setFrom_uid(int from_uid) {
            this.from_uid = from_uid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfile_picture() {
            return profile_picture;
        }

        public void setProfile_picture(String profile_picture) {
            this.profile_picture = profile_picture;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        @Override
        public String toString() {
            return "CommentEntity{" +
                    "id=" + id +
                    ", galleryId=" + galleryId +
                    ", create_time=" + create_time +
                    ", from_uid=" + from_uid +
                    ", content='" + content + '\'' +
                    ", username='" + username + '\'' +
                    ", profile_picture='" + profile_picture + '\'' +
                    ", floor='" + floor + '\'' +
                    '}';
        }
    }
}
