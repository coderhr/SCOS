package es.source.code.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import es.source.code.activity.FoodDetailedActivity;
import es.source.code.model.Data;
import es.source.code.activity.R;
import es.source.code.adapter.ListViewAdapter;
import es.source.code.model.User;

@SuppressLint("ValidFragment")
public class MyFragment extends Fragment implements ListViewAdapter.CallBack{

    private ArrayList<Data> data = null;
    Context mContext;
    ListViewAdapter listViewAdapter;
    private View view;
    private ListView listView;
    private int position;
    Data food;
    private static CallBack callback;
    User user;

    public interface CallBack{
        void event(Data data);
    }

    public static void setCallBack(CallBack callBack){
        callback = callBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_fragment, container,  false);
        if(data == null){
            data = new ArrayList<>();
        }

        listViewAdapter = new ListViewAdapter(mContext, data,this);
        listView = view.findViewById(R.id.food_layout_listview);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {    //其中l为item的position
                Data foodData = data.get(i);
                Intent intent = new Intent(getActivity(),FoodDetailedActivity.class);
                intent.putExtra("String", "FoodView");
                intent.putExtra("int", position);
                intent.putExtra("position", i);
                intent.putExtra("Food", foodData);
                intent.putExtra("User", user);
                intent.putExtra("FoodList", (Serializable)data);

                startActivity(intent);
            }
        });
        return view;
    }
    public void addData(ArrayList<Data> data){
        this.data = data;
    }

    public void setUser(User user){
        this.user = user;
    }

    @Override
    public void onClick(View view, boolean cancel){         //通过实现在listViewAdapter中的回调函数，接收从里面传回的被点击的位置信息

        if(cancel == false){
            data.get((Integer)view.getTag()).setOrder(true);
            food = data.get((Integer)view.getTag());
            food.setStoreNum((food.getStoreNum() - 1));
            food.setOrder(true);
            callback.event(food);
        }else{
            data.get((Integer)view.getTag()).setOrder(false);
            food = data.get((Integer)view.getTag());
            food.setStoreNum(food.getStoreNum() + 1);
            food.setOrder(false);
            callback.event(food);
        }
    }
    public Data getFood(){
        return food;
    }
    public void setPosintion(int position){
        this.position = position;
    }


}
