package es.source.code.model;

import java.io.Serializable;

public class Data implements Serializable{
    private String name;
    private int price;
    private boolean order;
    private String remark;
    private int num = 1;
    private int storeNum;

    public Data(String name, int price,boolean order, int storeNum){
        this.name = name;
        this.price = price;
        this.order = order;
        this.storeNum = storeNum;
    }

    public Data(String name, int price, boolean order, int num, String remark){
        this.name = name;
        this.price = price;
        this.order = order;
        this.num = num;
        this.remark = remark;
    }

    public boolean isOrder() {
        return order;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setOrder(boolean order) {
        this.order = order;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStoreNum() {
        return storeNum;
    }

    public Data(String name, int price){

        this.name = name;
        this.price = price;

        this.num = 1;
        this.remark = "";
        this.order = false;
    }

    public Data(int num , String name){
        this.num = num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean getButton() {
        return this.order;
    }

    public void setStoreNum(int storeNum){
        this.storeNum = storeNum;
    }
}
