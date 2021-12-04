package com.hust.agriculture.payload.request;

import com.hust.agriculture.model.Invoice;

import java.io.Serializable;

public class PaymentDTO implements Serializable {

    private String username;

    private Invoice[] invoices;

    private Long amount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Invoice[] getInvoices() {
        return invoices;
    }

    public void setInvoices(Invoice[] invoices) {
        this.invoices = invoices;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
