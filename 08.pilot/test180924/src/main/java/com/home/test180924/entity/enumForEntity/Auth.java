package com.home.test180924.entity.enumForEntity;

public enum Auth {
    ADMIN, USER;

    private Auth opposite;
    public Auth changeAuth(){
        ADMIN.opposite=USER;
        USER.opposite=ADMIN;
        return opposite;
    }
}
