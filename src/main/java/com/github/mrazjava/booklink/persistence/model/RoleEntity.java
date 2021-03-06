package com.github.mrazjava.booklink.persistence.model;

import javax.persistence.*;

/**
 * @author AZ
 */
@Entity(name = "bl_role")
public class RoleEntity {

    public static final long ID_DETECTIVE = 4l;

    @Id
    private Long id;

    @Column(name = "role_name")
    private String name;

    public RoleEntity() {

    }

    public RoleEntity(Long id) {
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
