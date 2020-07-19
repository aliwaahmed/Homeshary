package com.customer.shary.live.payment.order.ordermodel;

public class Datum {


    private String id                ;
    private String code              ;
    private String name              ;
    private String phone             ;
    private String quantity          ;
    private String address           ;
    private String price             ;
    private String user_id           ;
    private String delivery_method   ;
    private String payment_method    ;
    private String ship_fees         ;
    private String status            ;
    private String duration          ;
    private String reason            ;
    private String noti              ;
    private String cancel_at         ;
    private String delivered_at      ;
    private String confirm_at        ;
    private String store_id          ;
    private product product          ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getShip_fees() {
        return ship_fees;
    }

    public void setShip_fees(String ship_fees) {
        this.ship_fees = ship_fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }

    public String getCancel_at() {
        return cancel_at;
    }

    public void setCancel_at(String cancel_at) {
        this.cancel_at = cancel_at;
    }

    public String getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(String delivered_at) {
        this.delivered_at = delivered_at;
    }

    public String getConfirm_at() {
        return confirm_at;
    }

    public void setConfirm_at(String confirm_at) {
        this.confirm_at = confirm_at;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public com.customer.shary.live.payment.order.ordermodel.product getProduct() {
        return product;
    }

    public void setProduct(com.customer.shary.live.payment.order.ordermodel.product product) {
        this.product = product;
    }
}

   class product
   {
        private String  id                ;
        private String  code              ;
        private String  name              ;
        private String  category          ;
        private String  sub_category      ;
        private String  type              ;
        private String  price             ;
        private String  new_price         ;
        private String  image             ;
        private String  details           ;
        private String  installment       ;
        private String  available         ;
        private String  quantity          ;
        private String  status            ;
        private String  user_id           ;
        private String  product_link      ;
        private String  image_link        ;
        private String  video_link        ;
        private String  store_image       ;
        private String  store_name        ;
        private String  youtube           ;
        private String  rate_count        ;
        private String  created_at        ;
        private String  updated_at        ;


       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       public String getCode() {
           return code;
       }

       public void setCode(String code) {
           this.code = code;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getCategory() {
           return category;
       }

       public void setCategory(String category) {
           this.category = category;
       }

       public String getSub_category() {
           return sub_category;
       }

       public void setSub_category(String sub_category) {
           this.sub_category = sub_category;
       }

       public String getType() {
           return type;
       }

       public void setType(String type) {
           this.type = type;
       }

       public String getPrice() {
           return price;
       }

       public void setPrice(String price) {
           this.price = price;
       }

       public String getNew_price() {
           return new_price;
       }

       public void setNew_price(String new_price) {
           this.new_price = new_price;
       }

       public String getImage() {
           return image;
       }

       public void setImage(String image) {
           this.image = image;
       }

       public String getDetails() {
           return details;
       }

       public void setDetails(String details) {
           this.details = details;
       }

       public String getInstallment() {
           return installment;
       }

       public void setInstallment(String installment) {
           this.installment = installment;
       }

       public String getAvailable() {
           return available;
       }

       public void setAvailable(String available) {
           this.available = available;
       }

       public String getQuantity() {
           return quantity;
       }

       public void setQuantity(String quantity) {
           this.quantity = quantity;
       }

       public String getStatus() {
           return status;
       }

       public void setStatus(String status) {
           this.status = status;
       }

       public String getUser_id() {
           return user_id;
       }

       public void setUser_id(String user_id) {
           this.user_id = user_id;
       }

       public String getProduct_link() {
           return product_link;
       }

       public void setProduct_link(String product_link) {
           this.product_link = product_link;
       }

       public String getImage_link() {
           return image_link;
       }

       public void setImage_link(String image_link) {
           this.image_link = image_link;
       }

       public String getVideo_link() {
           return video_link;
       }

       public void setVideo_link(String video_link) {
           this.video_link = video_link;
       }

       public String getStore_image() {
           return store_image;
       }

       public void setStore_image(String store_image) {
           this.store_image = store_image;
       }

       public String getStore_name() {
           return store_name;
       }

       public void setStore_name(String store_name) {
           this.store_name = store_name;
       }

       public String getYoutube() {
           return youtube;
       }

       public void setYoutube(String youtube) {
           this.youtube = youtube;
       }

       public String getRate_count() {
           return rate_count;
       }

       public void setRate_count(String rate_count) {
           this.rate_count = rate_count;
       }

       public String getCreated_at() {
           return created_at;
       }

       public void setCreated_at(String created_at) {
           this.created_at = created_at;
       }

       public String getUpdated_at() {
           return updated_at;
       }

       public void setUpdated_at(String updated_at) {
           this.updated_at = updated_at;
       }
   }