package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Data;
import es.source.code.model.User;

public class FoodDetailedActivity extends Activity {

    private int position;
    private String string;
    private Data food;
    private List<Data> data;
    private static final int MIN_DISTANCE = 100; //左右最小滑动距离
    private ImageView imageView;
    float x1 = 0;
    float x2 = 0;
    User user = new User();

    TextView name;
    TextView  price;
    EditText remark;
    Button order;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detailed);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        remark = findViewById(R.id.remark);
        order = findViewById(R.id.button);
        back = findViewById(R.id.back);
        imageView = findViewById(R.id.image);

        Intent intent = getIntent();
        string = intent.getStringExtra("String");

        if(string.equals("UpdateService")){
            food = (Data)intent.getSerializableExtra("Data");
        } else {
            position = intent.getIntExtra("position", 0);
            food = (Data) intent.getSerializableExtra("Food");
            data = (List<Data>) intent.getSerializableExtra("FoodList");
            user = (User) getIntent().getSerializableExtra("User");
        }

        name.setText(food.getName());
        price.setText(String.valueOf(food.getPrice()));
        remark.setText(food.getRemark());

        switch (position){
            case 0:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food10));
                break;
            case 1:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food1));
                break;
            case 2:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food2));
                break;
            case 3:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food3));
                break;
            case 4:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food4));
                break;
            case 5:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food5));
                break;
            case 6:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food6));
                break;
            case 7:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food7));
                break;
            case 8:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food8));
                break;
            case 9:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.food9));
                break;
        }

        order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(food.isOrder()){
                    order.setText("点菜");
                    if(!string.equals("UpdateService")) {
                        data.get(position).setOrder(false);
                        food.setOrder(false);
                        user.deletUnOrderFood(food);
                        Toast.makeText(getApplicationContext(), "退点成功", Toast.LENGTH_SHORT).show();
                    }else{
                        food.setOrder(false);
                        user.deletUnOrderFood(food);
                        Toast.makeText(getApplicationContext(), "退点成功", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    order.setText("退点 ");
                    if(!string.equals("UpdateService")) {
                        data.get(position).setOrder(true);
                        food.setOrder(true);
                        user.addUnOrderFood(food);
                        Toast.makeText(getApplicationContext(), "点单成功", Toast.LENGTH_SHORT).show();
                    }else{
                        food.setOrder(true);
                        user.addUnOrderFood(food);
                        Toast.makeText(getApplicationContext(), "点单成功", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FoodDetailedActivity.this, FoodViewActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        if(food.isOrder()){
            order.setText("退点");
        }else{
            order.setText("点菜 ");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //按下手指时
            x1 = event.getRawX();
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            //抬起手指时
            x2 = event.getRawX();

            if((x1 - x2) > MIN_DISTANCE){
                if (position >= 0 && position < (data.size()-1)) {
                    position++;
                    switch (position){
                        case 0:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food0));
                            break;
                        case 1:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food1));
                            break;
                        case 2:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food2));
                            break;
                        case 3:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food3));
                            break;
                        case 4:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food4));
                            break;
                        case 5:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food5));
                            break;
                        case 6:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food6));
                            break;
                        case 7:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food7));
                            break;
                        case 8:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food8));
                            break;
                        case 9:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food9));
                            break;
                    }
                    food = data.get(position);
                    name.setText(food.getName());
                    price.setText(String.valueOf(food.getPrice()));
                    remark.setText(food.getRemark());

                    if (food.isOrder()) {
                        order.setText("退点");
                    } else {
                        order.setText("点菜");
                    }
                }
            }
            if((x2 - x1) > MIN_DISTANCE){
                if (position > 0 && position < (data.size())) {
                    position--;
                    switch (position){
                        case 0:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food0));
                            break;
                        case 1:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food1));
                            break;
                        case 2:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food2));
                            break;
                        case 3:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food3));
                            break;
                        case 4:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food4));
                            break;
                        case 5:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food5));
                            break;
                        case 6:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food6));
                            break;
                        case 7:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food7));
                            break;
                        case 8:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food8));
                            break;
                        case 9:
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.food9));
                            break;
                    }
                    food = data.get(position);
                    name.setText(food.getName());
                    price.setText(String.valueOf(food.getPrice()));
                    remark.setText(food.getRemark());

                    if (food.isOrder()) {
                        order.setText("退点");
                    } else {
                        order.setText("点菜");
                    }
                }
             }
        }
        return super.onTouchEvent(event);
    }

}

