package es.source.code.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;
import es.source.code.model.Data;
import es.source.code.activity.R;

public class UnOrderListAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private LinkedList<Data> data;
    private ViewHolderOne viewHolderOne;
    CallBack callBack;

    public UnOrderListAdapter(Context mContext, LinkedList<Data> data, CallBack callBack) {
        //this.mContext = mContext;
        this.data = data;
        this.callBack = callBack;
    }

    public interface CallBack{
        void Click(View v);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_food_layout, null);
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

        final TextWatcher onrelay_period = new MyTextWatcher() {
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    foodData.setRemark(null);
                } else {
                    foodData.setRemark(s.toString());
                }
            }
        };
        viewHolderOne.textView1.setText( foodData.getName());               //绑定数据
        viewHolderOne.textView2.setText(String.valueOf(foodData.getPrice()));
        viewHolderOne.textView3.setText(foodData.getRemark());
        viewHolderOne.textView4.setText(String.valueOf(foodData.getNum()));
        viewHolderOne.button.setTag(position);
        viewHolderOne.textView1.setTag(position);
        viewHolderOne.textView2.setTag(position);
        /*if(!foodData.getRemark().equals("")){
            viewHolderOne.textView3.setText(foodData.getRemark());
        }*/
        viewHolderOne.textView3.addTextChangedListener(onrelay_period);
        viewHolderOne.button.setOnClickListener(this);

        return convertView;
    }
    public void onClick(View view) {
        Toast.makeText(mContext,"退点成功",Toast.LENGTH_SHORT).show();
        callBack.Click(view);
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

    //监听备注栏
    public abstract class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

}
