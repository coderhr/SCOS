package es.source.code.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import es.source.code.model.Data;
import es.source.code.activity.R;
import es.source.code.adapter.UnOrderListAdapter;
import es.source.code.adapter.OrderListAdapter;
import es.source.code.model.User;

@SuppressLint("ValidFragment")
public class MyOrderFragment extends Fragment implements UnOrderListAdapter.CallBack,View.OnClickListener{
    private Context mContext;
    private View view;
    private ListView listView;
    private String fragmentType;
    private User user;
    private List<Data> unOrder = null;
    private List<Data> order = null;
    private static CallBack callBack;
    UnOrderListAdapter unOrderListAdapter;
    OrderListAdapter orderListAdapter;
    TextView totalNum;
    TextView totalPrice;
    Button submit;

    public interface CallBack{
        void event(String type);
        void event_cancel(Data data);
    }

    public static void setCallBack(CallBack callback) {
        callBack = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.order_fragment, container, false);
        if(unOrder == null || order == null){
            unOrder = new LinkedList<>();
            order = new LinkedList<>();
        }
        totalNum = view.findViewById(R.id.totalNum);
        totalPrice = view.findViewById(R.id.totalPrice);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(this);

        if(fragmentType.equals("unOrder")) {
            unOrderListAdapter = new UnOrderListAdapter(mContext, (LinkedList<Data>) unOrder,this);
            listView = view.findViewById(R.id.order_layout_listview);
            listView.setAdapter(unOrderListAdapter);
            submit.setText("提交订单");
            totalNum.setText("订单总数：" + CountNum(user.getUnOrderList()));
            totalPrice.setText("订单总价：" + CountPrice(user.getUnOrderList()));
            if(CountNum(user.getUnOrderList()) == 0){
                submit.setEnabled(false);
            }else{
                submit.setEnabled(true);
            }
        }else{
            orderListAdapter = new OrderListAdapter(mContext, (LinkedList<Data>) order);
            listView = view.findViewById(R.id.order_layout_listview);
            OrderListAdapter adapter = new OrderListAdapter(mContext, (LinkedList<Data>) order);
            listView.setAdapter(adapter);
            submit.setText("结账");
            totalNum.setText("订单总数：" + (CountNum(user.getOrderList())));
            totalPrice.setText("订单总价：" + (CountPrice(user.getOrderList())));
            if(CountNum(user.getOrderList()) == 0){
                submit.setEnabled(false);
            }else{
                submit.setEnabled(true);
            }
        }
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        return view;
    }

    public void setFragmentType(String fragmentType){
        this.fragmentType =  fragmentType;
    }

    public  void setUser(User user){
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }
    public void addUnOrder( List<Data> data) {
        this.unOrder = data;
    }

    public void addOrder( List<Data> data) {
        this.order = data;
    }

    public void onClick(View view){
        switch(fragmentType){
            case "unOrder":
                callBack.event(fragmentType);
                break;
            case "ordered":
                callBack.event(fragmentType);
                if (user.getOldUser() == true) {
                    Toast.makeText(getActivity(), "您好，老顾客，本次你可享受 7 折优惠", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void Click(View view){
        Data data = unOrder.get((Integer)view.getTag());
        callBack.event_cancel(data);
    }

    //计算总价格
    private static int CountPrice(List<Data> list) {
        int totalPrice = 0;
        if (list == null){
            return 0;
        }
        //遍历列表
        for (Data data : list) {
            totalPrice = totalPrice + data.getPrice();
        }
        return totalPrice;
    }

    //计算食物的总数
    private static int CountNum(List<Data> list) {
        return list.size();
    }

}