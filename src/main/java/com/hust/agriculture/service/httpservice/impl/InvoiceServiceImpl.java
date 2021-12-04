package com.hust.agriculture.service.httpservice.impl;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.*;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.CropRepository;
import com.hust.agriculture.repository.InvoiceRepository;
import com.hust.agriculture.repository.PaymentRepository;
import com.hust.agriculture.repository.UserRepository;
import com.hust.agriculture.service.httpservice.InvoiceService;
import com.hust.agriculture.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    CropRepository cropRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void scheduleCreateInvoice() {
        LOGGER.info("Start Schedule generate invoices");
        List<Crop> cropsByDay = cropRepository.findCropsMustPaidByDay();
        for(Crop crop : cropsByDay){
            for(Device device : crop.getDevices()){
                LOGGER.info("START: Generate invoice for device: "+ device.getDeviceName() +", crop: " + crop.getName());
                Invoice invoice = new Invoice();
                invoice.setCrop(crop);
                invoice.setDevice(device);
                invoice.setMoney(crop.getPlant().getUnitPricePerDay());
                invoice.setPaymentType(Constant.PAYMENT_BY_DAY);
                invoice.setUser(device.getFarm().getUser());
                invoice.setName(
                        CommonUtils.generateInvoiceName(Constant.PAYMENT_BY_DAY,
                                device.getDeviceName(),
                                crop.getName(),
                                device.getFarm().getUser().getUsername()));
                invoice.setDueDate(CommonUtils.add(Constant.DUE_DATE_DAY));
                invoice.setDescription("Generate by system !");
                invoice.setStatus(Constant.UNPAID_STATUS);
                invoice.setPlant(crop.getPlant());
                invoiceRepository.save(invoice);
                LOGGER.info("END: Generate invoice for device: "+ device.getDeviceName() +", crop: " + crop.getName());
            }
        }

        List<Crop> cropsByCrop = cropRepository.findCropsMustPaidByCrop();
        for(Crop crop : cropsByCrop){
            for(Device device : crop.getDevices()){
                LOGGER.info("START: Generate invoice for device: "+ device.getDeviceName() +", crop: " + crop.getName());
                Invoice invoice = new Invoice();
                invoice.setCrop(crop);
                invoice.setDevice(device);
                invoice.setMoney(crop.getPlant().getUnitPricePerCrop());
                invoice.setPaymentType(Constant.PAYMENT_BY_CROP);
                invoice.setUser(device.getFarm().getUser());
                invoice.setName(
                        CommonUtils.generateInvoiceName(Constant.PAYMENT_BY_CROP,
                                device.getDeviceName(),
                                crop.getName(),
                                device.getFarm().getUser().getUsername()));
                invoice.setDueDate(CommonUtils.add(Constant.DUE_DATE_CROP));
                invoice.setDescription("Generate by system !");
                invoice.setStatus(Constant.UNPAID_STATUS);
                invoice.setPlant(crop.getPlant());
                invoiceRepository.save(invoice);
                LOGGER.info("END: Generate invoice for device: "+ device.getDeviceName() +", crop: " + crop.getName());
            }
        }
        LOGGER.info("End Schedule generate invoices");
    }

    @Override
    public void getInvoices(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Invoice> pages = invoiceRepository.findInvoices(pageable, status, key);
        bean.addData("total", pages.getTotalElements());
        bean.addData("items", pages.getContent());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void paid(Long[] invoiceIds, Payment payment, ResponseBean bean) throws Exception {
        List<Invoice> invoices = new ArrayList<>();
        for(Long invoiceId : invoiceIds){
            Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
            if(invoice != null){
                invoice.setStatus(Constant.PAID_STATUS);
                invoice.setPayment(payment);
                invoices.add(invoice);
            }else{
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
                return;
            }
        }
        invoiceRepository.saveAll(invoices);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
    }

    @Override
    public void getInvoicesByUser(ResponseBean bean, Integer status, User user) throws Exception {
        List<Invoice> invoices = invoiceRepository.findByUser(status, user.getUsername());
        List<Payment> payments = paymentRepository.findPaymentNotComplete(user.getUsername());
        Long totalMoney = 0l;
        Long totalDebit = 0l;
        for(Invoice invoice : invoices){
            totalMoney += invoice.getMoney();
        }
        for(Payment payment : payments){
            totalDebit += payment.getDepositMoney();
        }
        bean.setError(Constant.ERROR_CODE_OK);
        bean.addData("invoices", invoices);
        bean.addData("totalInvoices", invoices.size());
        bean.addData("totalMoney", totalMoney);
        bean.addData("totalDebit", totalDebit);
        bean.addData("total", totalDebit+totalMoney);
    }

    @Override
    public void getInvoicesByUser(ResponseBean bean, Integer status, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if(user != null){
            getInvoicesByUser(bean, status, user);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }

    @Override
    public boolean isHasInvoiceOverDueDate(String username) {
        List<Invoice> invoices = invoiceRepository.findInvoicesOverDueDateByUsername(username);
        if(!ObjectUtils.isEmpty(invoices)){
            return true;
        }
        return false;
    }
}
