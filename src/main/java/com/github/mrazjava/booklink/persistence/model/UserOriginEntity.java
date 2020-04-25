package com.github.mrazjava.booklink.persistence.model;

import javax.persistence.*;

/**
 * @since 0.2.7
 */
@Entity(name = "bl_user_origin")
public class UserOriginEntity {

    public static final long ID_BOOKLINK_ORIGIN = 1;
    public static final long ID_FACEBOOK_ORIGIN = 2;
    public static final long ID_GOOGLE_ORIGIN = 3;

    @Id
    private Long id;

    @Column(name = "origin_name")
    private String name;

    public UserOriginEntity() {
    }

    public UserOriginEntity(long id) {
        this.id = id;
    }

    public UserOriginEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
