package es.source.code.model;

import java.util.ArrayList;
import java.util.List;

public class DataList {
    List<Data> coldFood;
    List<Data> hotFood;
    List<Data> seaFood;
    List<Data> drinkFood;

    public DataList(){
        this.coldFood = new ArrayList<>();
        this.hotFood = new ArrayList<>();
        this.seaFood = new ArrayList<>();
        this.drinkFood = new ArrayList<>();
    }

    public List<Data> getDrinkFood() {
        return drinkFood;
    }

    public void setDrinkFood(List<Data> drinkFood) {
        this.drinkFood = drinkFood;
    }

    public List<Data> getColdFood() {
        return coldFood;
    }

    public List<Data> getSeaFood() {
        return seaFood;
    }

    public void setSeaFood(List<Data> seaFood) {
        this.seaFood = seaFood;
    }

    public List<Data> getHotFood() {
        return hotFood;
    }

    public void setHotFood(List<Data> hotFood) {
        this.hotFood = hotFood;
    }

    public void setColdFood(List<Data> coldFood) {
        this.coldFood = coldFood;
    }
}
