package es.source.code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import es.source.code.model.Data;
import es.source.code.activity.R;

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Data> data;
    private ViewHolderOne viewHolderOne;
    CallBack callBack;

    /*创建一个构造器，分别传入上下文，食物的数据，和回调函数的值，和被点击的位置信息*/
    public ListViewAdapter(Context mContext, ArrayList<Data> data, CallBack callBack) {
        this.mContext = mContext;
        this.data = data;
        this.callBack = callBack;
    }
    /*定义一个回调函数*/
    public interface CallBack{
        void onClick(View view, boolean cancel);
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Data foodData = data.get(position);
        if(mContext == null){
            mContext = parent.getContext();
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout,null);             //定位布局文件
            viewHolderOne = new ViewHolderOne(convertView);
            viewHolderOne.textView1=convertView.findViewById(R.id.name);           //定位数据
            viewHolderOne.textView2=convertView.findViewById(R.id.price);
            viewHolderOne.button=convertView.findViewById(R.id.order);
            viewHolderOne.textView3=convertView.findViewById(R.id.number);
            convertView.setTag(viewHolderOne);                      //绑定数据，防止后面重复定位
        } else {
            viewHolderOne = (ViewHolderOne) convertView.getTag();
        }
        viewHolderOne.textView1.setText( foodData.getName());
        viewHolderOne.textView2.setText(String.valueOf(foodData.getPrice()));
        viewHolderOne.textView3.setText(String.valueOf(foodData.getStoreNum()));

        viewHolderOne.button.setTag(position);
        viewHolderOne.textView1.setTag(position);
        viewHolderOne.textView2.setTag(position);
        viewHolderOne.textView3.setTag(position);
        if(foodData.isOrder()==true){
            viewHolderOne.button.setText("退点");
        }else{
            viewHolderOne.button.setText("点菜");
        }
        viewHolderOne.button.setOnClickListener(this);
        return convertView;
    }
    public void onClick(View view) {
        if(data.get((Integer)view.getTag()).isOrder() == false){
            viewHolderOne.button.setText("退点");
            /*获取被点击的listview的位置信息，并通过声明函数的方式将信息传回*/
            callBack.onClick(view, false);
            Toast.makeText(mContext,"点单成功",Toast.LENGTH_SHORT).show();
        }else{
            viewHolderOne.button.setText("点菜");
            callBack.onClick(view, true);
            Toast.makeText(mContext,"退单成功",Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }

    class ViewHolderOne{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public Button button;

        public ViewHolderOne(View convertView){
            textView1 = convertView.findViewById(R.id.name);
            textView2 = convertView.findViewById(R.id.price);
            textView3 = convertView.findViewById(R.id.number);
            button = convertView.findViewById(R.id.order);
        }
    }
}
