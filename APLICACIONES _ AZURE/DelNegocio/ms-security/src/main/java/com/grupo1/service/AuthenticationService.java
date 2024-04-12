package com.grupo1.service;


import com.grupo1.request.RestorePwdRequest;
import com.grupo1.request.SignInRequest;
import com.grupo1.response.AuthenticationResponse;
import com.grupo1.response.ResponseBase;
import com.grupo1.response.VerifyResponse;

public interface AuthenticationService {


    AuthenticationResponse signin(SignInRequest iniciarSesionRequest);

    VerifyResponse getRoles(String token);

    ResponseBase SendCodeByEmail(String username);

    ResponseBase restorePassword(RestorePwdRequest restorePwdRequest);

}
