package org.aery.study.spring.mongo.service.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CollectionData1 {

    @Id
    private String id;

    private String brand;

    private String siteId;

    private Integer bulletCount;

    private Integer putIntoAmt;

    private Integer outPutAmt;

    private Long startTime;

    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Integer getBulletCount() {
        return bulletCount;
    }

    public void setBulletCount(Integer bulletCount) {
        this.bulletCount = bulletCount;
    }

    public Integer getPutIntoAmt() {
        return putIntoAmt;
    }

    public void setPutIntoAmt(Integer putIntoAmt) {
        this.putIntoAmt = putIntoAmt;
    }

    public Integer getOutPutAmt() {
        return outPutAmt;
    }

    public void setOutPutAmt(Integer outPutAmt) {
        this.outPutAmt = outPutAmt;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
