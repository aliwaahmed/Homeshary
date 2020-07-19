package com.customer.shary.live.Contacts;

public class model {

    private String name;
    private String phone;
    private String img;
    private String  exist;


    /**
     *
     * @param name
     * @param phone
     * @param img
     */
    public model(String name, String phone, String img,String exist) {
        this.name = name;
        this.phone = phone;
        this.img = img;
        this.exist=exist;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getExist() {
        return exist;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }
}
