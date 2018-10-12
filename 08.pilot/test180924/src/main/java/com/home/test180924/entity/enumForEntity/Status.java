package com.home.test180924.entity.enumForEntity;

public enum Status {
    ENABLE, DISABLE;

    private Status opposite;
    public Status changeStatus(){
        ENABLE.opposite = DISABLE;
        DISABLE.opposite = ENABLE;
        return opposite;
    }

}
