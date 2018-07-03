package com.tydic.anychatmeeting.bean;

/**
 * 作者：like on 2018/6/13 15:25
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：会议材料
 */
public class DocumentBean {

    private String id;
    private String created_by;
    private String created_time;
    private String file_path;
    private String file_size;
    private String file_type;
    private String meeting_id;
    private String file_name;
    private String meeting_name;
    private String file_password;
    private String is_favorited;
    private String water_content;
    private String html_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public String getFile_password() {
        return file_password;
    }

    public void setFile_password(String file_password) {
        this.file_password = file_password;
    }

    public String getIs_favorited() {
        return is_favorited;
    }

    public void setIs_favorited(String is_favorited) {
        this.is_favorited = is_favorited;
    }

    public String getWater_content() {
        return water_content;
    }

    public void setWater_content(String water_content) {
        this.water_content = water_content;
    }

    public String getHtml_path() {
        return html_path;
    }

    public void setHtml_path(String html_path) {
        this.html_path = html_path;
    }

    @Override
    public String toString() {
        return "DocumentBean{" +
                "id='" + id + '\'' +
                ", created_by='" + created_by + '\'' +
                ", created_time='" + created_time + '\'' +
                ", file_path='" + file_path + '\'' +
                ", file_size='" + file_size + '\'' +
                ", file_type='" + file_type + '\'' +
                ", meeting_id='" + meeting_id + '\'' +
                ", file_name='" + file_name + '\'' +
                ", meeting_name='" + meeting_name + '\'' +
                ", file_password='" + file_password + '\'' +
                ", is_favorited='" + is_favorited + '\'' +
                ", water_content='" + water_content + '\'' +
                ", html_path='" + html_path + '\'' +
                '}';
    }
}
