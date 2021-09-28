package com.aditya.myblogproject.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostTagCompositeId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id")
    private int postId;
    @Column(name = "tag_id")
    private int tagId;

    public PostTagCompositeId() {
    }

    public PostTagCompositeId(int postId, int tagId) {
        this.postId = postId;
        this.tagId = tagId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostTagCompositeId that = (PostTagCompositeId) o;
        return postId == that.postId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, tagId);
    }
}
