package com.hust.agriculture.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PLANT")
public class Plant implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_AT", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "UNIT_PRICE_PER_CROP")
    private Long unitPricePerCrop;

    @Column(name = "UNIT_PRICE_PER_DAY")
    private Long unitPricePerDay;

    @Column(name = "STATUS")
    private Integer status;

    public Long getUnitPricePerCrop() {
        return unitPricePerCrop;
    }

    public void setUnitPricePerCrop(Long unitPricePerCrop) {
        this.unitPricePerCrop = unitPricePerCrop;
    }

    public Long getUnitPricePerDay() {
        return unitPricePerDay;
    }

    public void setUnitPricePerDay(Long unitPricePerDay) {
        this.unitPricePerDay = unitPricePerDay;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
