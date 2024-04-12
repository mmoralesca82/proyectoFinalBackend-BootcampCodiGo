package com.grupo1.infraestructure.client;



import com.grupo1.domain.aggregates.response.MsExternalToHInsuranceRimacResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "MS-EXTERNALAPI", url="${url.externalapi}")
public interface ToMSExternalApi {

//    @GetMapping("/reniec/{numero}")
//    MsExternalToReniecResponse getInfoExtReniec(@PathVariable String numero);
    @Async	
    @GetMapping("/ms-externalapi/v1/insurance/rimac")
    MsExternalToHInsuranceRimacResponse getInfoExtRimac(@RequestParam String documento, @RequestParam String cobertura);

}
