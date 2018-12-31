package com.example.android.inventoryapp.Product;

public class Product {
    private String productName;
    private float productPrice;
    private String productImage;
    private int productQuantity;
    private int productSold;
    private String productSupplier;

    public Product(String productName,
                   float productPrice,
                   String productImage,
                   int productQuantity,
                   int productSold,
                   String productSupplier) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
        this.productSold = productSold;
        this.productSupplier = productSupplier;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductSold() {
        return productSold;
    }

    public void setProductSold(int productSold) {
        this.productSold = productSold;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productImage='" + productImage + '\'' +
                ", productQuantity=" + productQuantity +
                ", productSold=" + productSold +
                ", productSupplier='" + productSupplier + '\'' +
                '}';
    }

}