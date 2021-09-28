package com.aditya.myblogproject.models;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = " comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int commentId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private String  createDate;
    @Column(name = "updated_at")
    private  String updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    public Comment() {
    }

    public Comment(int commentId, String userName, String email, String comment, Post post, String createDate, String updateDate) {
        this.commentId = commentId;
        this.userName = userName;
        this.email = email;
        this.comment = comment;
        this.post = post;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
