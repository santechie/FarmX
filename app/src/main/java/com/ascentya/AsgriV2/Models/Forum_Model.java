package com.ascentya.AsgriV2.Models;

public class Forum_Model {
    String forum_id, forum_title, forum_posterid, forum_description, category, is_active, forum_attachment, created_at, user_name, is_liked;

    public String getForum_id() {
        return forum_id;
    }

    public String getForum_posterid() {
        return forum_posterid;
    }

    public void setForum_posterid(String forum_posterid) {
        this.forum_posterid = forum_posterid;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getForum_title() {
        return forum_title;
    }

    public void setForum_title(String forum_title) {
        this.forum_title = forum_title;
    }

    public String getForum_description() {
        return forum_description;
    }

    public void setForum_description(String forum_description) {
        this.forum_description = forum_description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getForum_attachment() {
        return forum_attachment;
    }

    public void setForum_attachment(String forum_attachment) {
        this.forum_attachment = forum_attachment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }
}
