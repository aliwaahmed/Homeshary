package com.customer.shary.live.notification;

import com.customer.shary.live.ui.home.datamodel.apidata;

public class datamodel extends apidata {

   private String id;
   private String title;
   private String message;
   private String seller_id;
   private String seller_image;
   private String product_id;
   private String product_imag;
   private String delivered;
   private String noti_read;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public void setSeller_image(String seller_image) {
        this.seller_image = seller_image;
    }


    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_imag() {
        return product_imag;
    }

    public void setProduct_imag(String product_imag) {
        this.product_imag = product_imag;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getNoti_read() {
        return noti_read;
    }

    public void setNoti_read(String noti_read) {
        this.noti_read = noti_read;
    }
}
