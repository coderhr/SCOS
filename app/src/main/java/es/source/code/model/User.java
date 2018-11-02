package es.source.code.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable{
    // 定义用户名，密码，是否为老用户
    private String userName;
    private String password;
    private boolean oldUser;
    private List<Data> unOrderFood;
    private List<Data> orderFood;
    private DataList dataList;

    public User(){
        this.userName = "temp";
        this.password = "0";
        this.unOrderFood = new LinkedList<>();
        this.orderFood = new LinkedList<>();
        this.oldUser = false;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.oldUser = false;
        this.unOrderFood = new LinkedList<>();
        this.orderFood = new LinkedList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Str){
        this.password = Str;
    }

    public boolean getOldUser() {
        return oldUser;
    }

    public void setOldUser(boolean b){
        this.oldUser = b;
    }


    public void deletUnOrderFood(Data data){
        Iterator<Data> it = unOrderFood.iterator();
        while(it.hasNext()){
            Data f= it.next();
            if(f.getName().equals(data.getName())){
                if(f.getNum()>1){
                    f.setNum(f.getNum()-1);
                }
                else {
                    it.remove();
                }
                break;
            }
        }
    }

    public void addUnOrderFoodList(List<Data> data){
        this.unOrderFood.addAll(data);
    }

    public void addOrderFoodList(List<Data>data){
        this.orderFood.addAll(data);
    }

    public void addUnOrderFood(Data data){
        this.unOrderFood.add(data);
    }

    public void clearUnOrderList(){
        this.unOrderFood.clear();
    }

    public void clearOrderList(){
        this.orderFood.clear();
    }

    public  List<Data> getUnOrderList(){
        return this.unOrderFood;
    }

    public List<Data> getOrderList(){
        return this.orderFood;
    }

    public void setDataList(DataList dataList){
        this.dataList = dataList;
    }

    public DataList getDataList(){
        return dataList;
    }

}