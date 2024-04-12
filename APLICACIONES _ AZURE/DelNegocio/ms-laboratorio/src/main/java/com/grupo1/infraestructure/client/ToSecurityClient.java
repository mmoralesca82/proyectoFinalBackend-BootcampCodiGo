package com.grupo1.infraestructure.client;


import com.grupo1.domain.aggregates.response.MsSecurityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Optional;



@FeignClient(name="MS-SECURITY", url="${url.mssecurity}")
public interface ToSecurityClient {

    @Async
    @PostMapping("/ms-security/v1/autenticacion/verify")
    Optional<MsSecurityResponse> verifyToken (@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

}
