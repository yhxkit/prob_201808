package com.home.test180924.entity;


import com.home.test180924.entity.enumForEntity.Auth;
import com.home.test180924.entity.enumForEntity.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountDto {

    @NotNull
    private String email;
    private String password;
    private String name;

    private Auth auth= Auth.USER;
    private Status status = Status.ENABLE;

}
