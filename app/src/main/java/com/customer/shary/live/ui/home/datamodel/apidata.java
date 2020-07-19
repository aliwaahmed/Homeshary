package com.customer.shary.live.ui.home.datamodel;

import android.os.Parcel;
import android.os.Parcelable;


import com.customer.shary.live.CustomRecyclerView.MediaObject;

import java.util.ArrayList;



public class apidata extends ads implements Parcelable {

    MediaObject mediaObject ;


    String Seller_id;
    String code ;
    String name ;
    ArrayList image ;
    String video_thumb ;
    ArrayList<String> video;
     int comment_number;
    String category ;
    String sub_category ;
    String type ;
    String price ;
    String new_price ;
    String details ;
    String installment ;
    String available ;
    String quantity ;
    String status ;
    int product_id;
    String created_at ;
    String updated_at ;
    String favorites ;
    String store_image ;
    String store_name ;
    String product_link;
    String views ;
    String share ;
    String rate_count ;
    String image_link ;
    String video_link ;
    int mediatype;

    String likes ;
    String dislikes ;

    String user_like ;
    String user_dislike ;
    String user_share ;
    String user_store_follow ;
    String user_rate;
   String Seller_room;


    public apidata() {
    }

    protected apidata(Parcel in) {
        mediaObject = in.readParcelable(MediaObject.class.getClassLoader());
        Seller_id = in.readString();
        code = in.readString();
        name = in.readString();
        video_thumb = in.readString();
        video = in.createStringArrayList();
        comment_number = in.readInt();
        category = in.readString();
        sub_category = in.readString();
        type = in.readString();
        price = in.readString();
        new_price = in.readString();
        details = in.readString();
        installment = in.readString();
        available = in.readString();
        quantity = in.readString();
        status = in.readString();
        product_id = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        favorites = in.readString();
        store_image = in.readString();
        store_name = in.readString();
        product_link = in.readString();
        views = in.readString();
        share = in.readString();
        rate_count = in.readString();
        image_link = in.readString();
        video_link = in.readString();
        mediatype = in.readInt();
        likes = in.readString();
        dislikes = in.readString();
        user_like = in.readString();
        user_dislike = in.readString();
        user_share = in.readString();
        user_store_follow = in.readString();
        user_rate = in.readString();
        Seller_room=in.readString();
    }

    public static final Creator<apidata> CREATOR = new Creator<apidata>() {
        @Override
        public apidata createFromParcel(Parcel in) {
            return new apidata(in);
        }

        @Override
        public apidata[] newArray(int size) {
            return new apidata[size];
        }
    };

    public MediaObject getMediaObject() {
        return mediaObject;
    }

    public void setMediaObject(MediaObject mediaObject) {
        this.mediaObject = mediaObject;
    }

    public String getSeller_id() {
        return Seller_id;
    }

    public void setSeller_id(String seller_id) {
        Seller_id = seller_id;
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

    public ArrayList getImage() {
        return image;
    }

    public void setImage(ArrayList image) {
        this.image = image;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public ArrayList<String> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<String> video) {
        this.video = video;
    }

    public int getComment_number() {
        return comment_number;
    }

    public void setComment_number(int comment_number) {
        this.comment_number = comment_number;
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
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

    public String getProduct_link() {
        return product_link;
    }

    public void setProduct_link(String product_link) {
        this.product_link = product_link;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getRate_count() {
        return rate_count;
    }

    public void setRate_count(String rate_count) {
        this.rate_count = rate_count;
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

    public int getMediatype() {
        return mediatype;
    }

    public void setMediatype(int mediatype) {
        this.mediatype = mediatype;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getUser_like() {
        return user_like;
    }

    public void setUser_like(String user_like) {
        this.user_like = user_like;
    }

    public String getUser_dislike() {
        return user_dislike;
    }

    public void setUser_dislike(String user_dislike) {
        this.user_dislike = user_dislike;
    }

    public String getUser_share() {
        return user_share;
    }

    public void setUser_share(String user_share) {
        this.user_share = user_share;
    }

    public String getUser_store_follow() {
        return user_store_follow;
    }

    public void setUser_store_follow(String user_store_follow) {
        this.user_store_follow = user_store_follow;
    }

    public String getUser_rate() {
        return user_rate;
    }

    public void setUser_rate(String user_rate) {
        this.user_rate = user_rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSeller_room() {
        return Seller_room;
    }

    public void setSeller_room(String seller_room) {
        Seller_room = seller_room;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mediaObject, i);
        parcel.writeString(Seller_id);
        parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(video_thumb);
        parcel.writeStringList(video);
        parcel.writeInt(comment_number);
        parcel.writeString(category);
        parcel.writeString(sub_category);
        parcel.writeString(type);
        parcel.writeString(price);
        parcel.writeString(new_price);
        parcel.writeString(details);
        parcel.writeString(installment);
        parcel.writeString(available);
        parcel.writeString(quantity);
        parcel.writeString(status);
        parcel.writeInt(product_id);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(favorites);
        parcel.writeString(store_image);
        parcel.writeString(store_name);
        parcel.writeString(product_link);
        parcel.writeString(views);
        parcel.writeString(share);
        parcel.writeString(rate_count);
        parcel.writeString(image_link);
        parcel.writeString(video_link);
        parcel.writeInt(mediatype);
        parcel.writeString(likes);
        parcel.writeString(dislikes);
        parcel.writeString(user_like);
        parcel.writeString(user_dislike);
        parcel.writeString(user_share);
        parcel.writeString(user_store_follow);
        parcel.writeString(user_rate);
        parcel.writeString(Seller_room);
    }
}
