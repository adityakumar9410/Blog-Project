package com.aditya.myblogproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tagId;
    @Column(name = "tag_name")
    private String tagName;
    @Column(name = "created_at")
    private Date createDate = null;
    @Column(name = "updated_at")
    private  Date updateDate = null;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tags")
    private List<Post>posts = new ArrayList<>();



    public Tag() {
    }

    public Tag(int tagId, String tagName, Date createDate, Date updateDate, List<Post> posts) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.posts = posts;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
