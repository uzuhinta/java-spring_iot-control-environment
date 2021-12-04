package com.hust.agriculture.service.httpservice.impl;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Invoice;
import com.hust.agriculture.model.Payment;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.PaymentDTO;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.InvoiceRepository;
import com.hust.agriculture.repository.PaymentRepository;
import com.hust.agriculture.repository.UserRepository;
import com.hust.agriculture.service.httpservice.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public void paid(PaymentDTO paymentDTO, ResponseBean bean) throws Exception {

        if(paymentDTO.getAmount() <= 0){
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_PAYMENT_AMOUNT_INVALID);
            return;
        }

        String username = paymentDTO.getUsername();
        User user = userRepository.findByUsername(username);
        if(user != null){
            Invoice[] invoices = paymentDTO.getInvoices();
            Long amount = paymentDTO.getAmount();
            List<Invoice> rootInvoices = new ArrayList<>();
            for(Invoice invoice : invoices){
                List<Invoice> list = invoiceRepository.findByInvoiceIdAndUserId(invoice.getID(), user.getID());
                if(list != null && list.size() > 0){
                    rootInvoices.add(list.get(0));
                }else{
                    bean.setError(Constant.ERROR_CODE_NOK);
                    bean.setMessage(Constant.MSG_INVOICE_NOT_EXISTS);
                    return;
                }
            }
            Long rootAmount = 0l;
            for(Invoice invoice : rootInvoices){
                rootAmount += invoice.getMoney();
            }

            List<Payment> payments = paymentRepository.findPaymentNotComplete(user.getUsername());
            Long totalDebit = 0l;
            for(Payment payment : payments){
                totalDebit += payment.getDepositMoney();
            }

            rootAmount += totalDebit;

            Payment payment = new Payment();
            payment.setPaymentMethod(Constant.PAYMENT_METHOD_ONLINE);
            payment.setPaidMoney(amount);
            payment.setDepositMoney(rootAmount - amount);
            payment.setTotalMoney(rootAmount);
            payment.setUser(user);
            payment.setDescription("Pay via admin");
            if(rootAmount - amount == 0){
                payment.setStatus(Constant.PAID_STATUS);
            }else{
                payment.setStatus(Constant.UNPAID_STATUS);
            }
            paymentRepository.save(payment);

            for(int i=0; i<payments.size(); i++){
                payments.get(i).setStatus(Constant.PAID_STATUS);
            }
            paymentRepository.saveAll(payments);

            for(int i=0; i<rootInvoices.size(); i++){
                rootInvoices.get(i).setPayment(payment);
                rootInvoices.get(i).setStatus(Constant.PAID_STATUS);
            }

            invoiceRepository.saveAll(rootInvoices);

            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_PAID_SUCCESS);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }
}
