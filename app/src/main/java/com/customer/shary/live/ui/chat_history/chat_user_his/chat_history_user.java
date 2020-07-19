package com.customer.shary.live.ui.chat_history.chat_user_his;

public class chat_history_user {


     private String room;
     private String admin_id ;
     private String admin_name;
     private String admin_image;
     private String latest_message;
     private int blocked ;
     private String Saller_id;

    public String getSaller_id() {
        return Saller_id;
    }

    public void setSaller_id(String saller_id) {
        Saller_id = saller_id;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public String getLatest_message() {
        return latest_message;
    }

    public void setLatest_message(String latest_message) {
        this.latest_message = latest_message;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_image() {
        return admin_image;
    }

    public void setAdmin_image(String admin_image) {
        this.admin_image = admin_image;
    }
}
