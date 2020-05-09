package com.github.mrazjava.booklink.persistence.model;

/**
 * @author AZ
 */
public enum UserOrigin {
    BOOKLINK(1),
    FACEBOOK(2),
    GOOGLE(3);

    private long id;
    private UserOrigin(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static UserOrigin fromId(long id) {
        if(id == 1)
            return BOOKLINK;
        else if(id == 2)
            return FACEBOOK;
        else if(id == 3)
            return GOOGLE;
        else
            throw new IllegalArgumentException("invalid id");
    }
}
