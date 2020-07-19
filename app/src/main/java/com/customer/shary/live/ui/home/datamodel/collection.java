package com.customer.shary.live.ui.home.datamodel;

public class collection extends apidata {
     String typeee;
     apidata apidata ;
     ads ads ;


    public String getTypeee() {
        return typeee;
    }

    public void setTypeee(String typeee) {
        this.typeee = typeee;
    }

    public com.customer.shary.live.ui.home.datamodel.apidata getApidata() {
        return apidata;
    }

    public void setApidata(com.customer.shary.live.ui.home.datamodel.apidata apidata) {
        this.apidata = apidata;
    }

    public com.customer.shary.live.ui.home.datamodel.ads getAds() {
        return ads;
    }

    public void setAds(com.customer.shary.live.ui.home.datamodel.ads ads) {
        this.ads = ads;
    }
}
