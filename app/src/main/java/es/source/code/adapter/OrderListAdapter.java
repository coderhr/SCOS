package es.source.code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import es.source.code.model.Data;
import es.source.code.activity.R;

public class OrderListAdapter extends BaseAdapter{
    private ViewHolderOne viewHolderOne;
    private Context mContext;
    private LinkedList<Data> data;

    public OrderListAdapter(Context mContext, LinkedList<Data> data) {
        this.mContext = mContext;
        this.data = data;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_food_layout1, null);
            viewHolderOne = new ViewHolderOne(convertView);
            viewHolderOne.textView1=convertView.findViewById(R.id.name);
            viewHolderOne.textView2=convertView.findViewById(R.id.price);
            viewHolderOne.textView3=convertView.findViewById(R.id.remark);
            viewHolderOne.textView4=convertView.findViewById(R.id.number);
            viewHolderOne.button=convertView.findViewById(R.id.order);
            convertView.setTag(viewHolderOne);
        } else {
            viewHolderOne = (ViewHolderOne) convertView.getTag();
        }
        viewHolderOne.textView1.setText( foodData.getName());               //绑定数据
        viewHolderOne.textView2.setText(String.valueOf(foodData.getPrice()));
        viewHolderOne.textView3.setText(foodData.getRemark());
        viewHolderOne.textView4.setText(String.valueOf(foodData.getNum()));

        return convertView;
    }

    class ViewHolderOne{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public Button button;

        public ViewHolderOne(View convertView){
            textView1 = convertView.findViewById(R.id.name);
            textView2 = convertView.findViewById(R.id.price);
            textView3 = convertView.findViewById(R.id.remark);
            textView4 = convertView.findViewById(R.id.number);
            button = convertView.findViewById(R.id.order);
        }
    }

}
