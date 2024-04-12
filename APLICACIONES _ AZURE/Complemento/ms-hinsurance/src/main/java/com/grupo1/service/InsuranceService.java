package com.grupo1.service;


import com.grupo1.request.InsuranceRequest;
import com.grupo1.response.InsuranceResponse;

public interface InsuranceService {

    InsuranceResponse askForInsured(InsuranceRequest insuranceRequest, String token);
}
