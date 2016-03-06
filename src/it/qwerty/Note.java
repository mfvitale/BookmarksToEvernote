package it.qwerty;

/**
 * Created by marvi on 06/03/2016.
 */
public class Note {
    private String title;
    private String href;
    private String resourceBase64;
    private String resourceMimeType;
    private String resourceMD5;
    private String creationDate;
    private String tag;

    public Note(){
    }
    public Note(String title, String href, String resourceBase64, String resourceMimeType, String creationDate, String tag) {
        this.title = title;
        this.href = href;
        this.resourceBase64 = resourceBase64;
        this.resourceMimeType = resourceMimeType;
        this.creationDate = creationDate;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getResourceBase64() {
        return resourceBase64;
    }

    public void setResourceBase64(String resourceBase64) {
        this.resourceBase64 = resourceBase64;
    }

    public String getResourceMimeType() {
        return resourceMimeType;
    }

    public void setResourceMimeType(String resourceMimeType) {
        this.resourceMimeType = resourceMimeType;
    }

    public String getResourceMD5() {
        return resourceMD5;
    }

    public void setResourceMD5(String resourceMD5) {
        this.resourceMD5 = resourceMD5;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
