package com.example.hmwl;

public class MyOrderItemModel {

    private int productImage;
    private String productTitle;
    private String deliveryStatus;
    private String orderAddress;

    private int rating;

    public int getProductImage() {
        return productImage;
    }

    public int getRating() {
        return rating;
    }

//    public void setRating(int rating) {
//        this.rating = rating;
//    }
//
//    public void setProductImage(int productImage) {
//        this.productImage = productImage;
//    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public MyOrderItemModel(int productImage, String productTitle, String deliveryStatus, String orderAddress) {
        this.productImage = productImage;
//        this.rating= rating;
        this.productTitle = productTitle;
        this.deliveryStatus = deliveryStatus;
        this.orderAddress = orderAddress;
    }
}
