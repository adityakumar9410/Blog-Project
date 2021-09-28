package com.aditya.myblogproject.models;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "post_tags")
public class PostTag  {

    @EmbeddedId
    private PostTagCompositeId id;

    @Column(name = "created_at")
    private Date createDate = new Date(System.currentTimeMillis());

    @Column(name = "updated_at")
    private Date updateDate = new Date((System.currentTimeMillis()));

    public PostTag() {
    }

    public PostTag(PostTagCompositeId id) {
        this.id = id;
    }

    public PostTagCompositeId getId() {
        return id;
    }

    public void setId(PostTagCompositeId id) {
        this.id = id;
    }
}
