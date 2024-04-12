package com.grupo1.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestorePwdRequest {

    private String username;
    private String token;
    private String newPassword;
}
