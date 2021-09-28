package com.aditya.myblogproject.models;

import javax.persistence.*;


@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tagId;
    @Column(name = "tag_name", unique = true)
    private String tagName;
    @Column(name = "created_at")
    private  String createDate;
    @Column(name = "updated_at")
    private  String updateDate;

    public Tag() {
    }

    public Tag(int tagId, String tagName, String createDate, String updateDate) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }



    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
