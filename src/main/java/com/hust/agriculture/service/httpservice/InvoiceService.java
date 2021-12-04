package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.model.Payment;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.response.ResponseBean;

public interface InvoiceService {

    void scheduleCreateInvoice();

    void getInvoices(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

    void paid(Long[] invoiceIds, Payment payment, ResponseBean bean) throws Exception;

    void getInvoicesByUser(ResponseBean bean, Integer status, User user) throws Exception;
    void getInvoicesByUser(ResponseBean bean, Integer status, String username) throws Exception;

    boolean isHasInvoiceOverDueDate(String username);

}
