package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.payload.request.PaymentDTO;
import com.hust.agriculture.payload.response.ResponseBean;

public interface PaymentService {

    void paid(PaymentDTO paymentDTO, ResponseBean bean) throws Exception;
}
