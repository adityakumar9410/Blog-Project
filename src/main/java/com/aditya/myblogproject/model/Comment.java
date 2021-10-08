package com.aditya.myblogproject.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

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
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comment" , columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at",  updatable = false)
    private Date createDate=null;
    @Column(name = "updated_at")
    private  Date  updateDate= null;

    public Comment() {
    }

    public Comment(int commentId, String userName, String email, String comment, Date createDate, Date updateDate) {
        this.commentId = commentId;
        this.userName = userName;
        this.email = email;
        this.comment = comment;
        this.createDate = createDate;
        this.updateDate = updateDate;
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
