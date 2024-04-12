package com.grupo1.service.impl;

import com.grupo1.entity.UsuarioEntity;
import com.grupo1.repository.UsuarioRepository;
import com.grupo1.request.RestorePwdRequest;
import com.grupo1.request.SignInRequest;
import com.grupo1.response.AuthenticationResponse;
import com.grupo1.response.ResponseBase;
import com.grupo1.response.VerifyResponse;
import com.grupo1.service.AuthenticationService;
import com.grupo1.service.JWTService;
import com.grupo1.util.EmailService;
import com.grupo1.util.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl  implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final RedisService redisService;

    @Override
    public AuthenticationResponse signin(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(), signInRequest.getPassword()));
        var user = usuarioRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no valido"));

        var jwt = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        return authenticationResponse;
    }

    @Override
    public VerifyResponse getRoles(String subjectFromToken) {
        Optional<UsuarioEntity> findUser = usuarioRepository.findByUsername(subjectFromToken);
        if (findUser.isPresent()) {
            Set<String> roles = findUser.get().getRolesNames();
            return new VerifyResponse(200, "allowed",
                    findUser.get().getUsername(), roles);

        }
        return new VerifyResponse(404, null, null, null);
    }

    @Override
    public ResponseBase SendCodeByEmail(String username) {
        Optional<UsuarioEntity> getUser = usuarioRepository.findByUsername(username);
        if (getUser.isPresent()) {
            Random r = new Random();
            int token = r.nextInt(900000) + 100000;
            if(!redisService.saveRedis(username, Integer.toString(token), 2)){
                return new ResponseBase(500, "Falla de conexion con redis-server.");
            }
            if (emailService.SendEmail(getUser.get().getEmail(), "Recuperacion de acceso.",
                    "Codigo para restaurar contraseña: " + token))
                return new ResponseBase(200, "Correo enviado con exito.");
            return new ResponseBase(500, "Error al enviar correo.");
        }
        return new ResponseBase(400, "Username no existe.");
    }

    @Override
    public ResponseBase restorePassword(RestorePwdRequest restorePwdRequest) {
        Optional<UsuarioEntity> getUser = usuarioRepository.findByUsername(restorePwdRequest.getUsername());
        if (getUser.isPresent()) {
            String getFromRedis = redisService.getFromRedis(restorePwdRequest.getUsername());
            if(getFromRedis.equals(restorePwdRequest.getToken())) {
                redisService.deleteKey(restorePwdRequest.getUsername());
                getUser.get().setPassword(new BCryptPasswordEncoder().encode(restorePwdRequest.getNewPassword()));
                try{
                    usuarioRepository.save(getUser.get());
                    return new ResponseBase(200, "Contraseña actualizada con exito.");
                }catch (Exception e){
                    return new ResponseBase(500, "Error al actualizar nueva contraseña.");
                }
            }
            return new ResponseBase(500, "Error de codigo de validacion.");
        }
        return new ResponseBase(400, "Username no existe.");
    }
}