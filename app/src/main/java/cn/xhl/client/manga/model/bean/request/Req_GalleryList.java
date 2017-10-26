package cn.xhl.client.manga.model.bean.request;

/**
 * @author Mike on 2017/10/23 0023.
 */
public class Req_GalleryList {
    /**
     * 书籍的类型
     */
    private String category;
    /**
     * 页码
     */
    private int page;
    /**
     * 每页的长度
     */
    private int size;
    /**
     * 请求的类型
     */
    private String type;

    public Req_GalleryList() {
    }

    public Req_GalleryList(String category, int page, int size, String type) {
        this.category = category;
        this.page = page;
        this.size = size;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Req_GalleryList{" +
                "category='" + category + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
}
