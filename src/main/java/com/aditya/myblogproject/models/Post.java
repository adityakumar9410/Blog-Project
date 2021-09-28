package com.aditya.myblogproject.models;

import javax.persistence.*;

import java.util.Date;


@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(name = "post_title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "excerpt")
    private String excerpt;


    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "published_at")
    private Date publishDate = new Date(System.currentTimeMillis());


    @Column(name = "created_at")
    private Date createDate = new Date(System.currentTimeMillis());

    @Column(name = "updated_at")
    private Date updateDate = new Date((System.currentTimeMillis()));

    public Post() {
    }

    public Post(int postId, String title, String author, String excerpt, String content, boolean isPublished, Date publishDate,
                Date createDate, Date updateDate) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.excerpt = excerpt;
        this.content = content;
        this.isPublished = isPublished;
        this.publishDate = publishDate;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
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
}
