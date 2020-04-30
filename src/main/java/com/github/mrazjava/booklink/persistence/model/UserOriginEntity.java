package com.github.mrazjava.booklink.persistence.model;

import javax.persistence.*;

/**
 * @since 0.2.7
 */
@Entity(name = "bl_user_origin")
public class UserOriginEntity {

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
