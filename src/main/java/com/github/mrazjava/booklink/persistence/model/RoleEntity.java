package com.github.mrazjava.booklink.persistence.model;

import javax.persistence.*;

/**
 * @since 0.2.0
 */
@Entity(name = "bl_role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

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
