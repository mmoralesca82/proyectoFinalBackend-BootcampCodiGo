package com.grupo1.msexternalapi.infraestructure.rest;

import com.grupo1.msexternalapi.domain.response.ResponseReniec;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-reniec", url ="${url.reniec}")
public interface ReniecClient {

    @GetMapping("/dni")
    ResponseReniec getInfoReniec(@RequestParam("numero") String numero,
                                 @RequestHeader("Authorization") String authorizationHeader);
}

