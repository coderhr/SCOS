package es.source.code.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import es.source.code.fragment.MyOrderFragment;
import es.source.code.model.Data;
import es.source.code.model.User;

public class FoodOrderViewActivity extends AppCompatActivity implements MyOrderFragment.CallBack{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<String> list;
    private int tab;
    private String[] titles = {"未下单菜", "已下单菜"};
    User user;
    MyOrderFragment myOrderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_view);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        //fragments1 = new ArrayList<>();

        tab = getIntent().getExtras().getInt("tab");
        user = (User)getIntent().getSerializableExtra("User");
        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);
        MyOrderFragment.setCallBack(this);

        //设置ViewPager的适配器
        //adapter = new OrderViewAdapter(getSupportFragmentManager(),fragments1);
        //viewPager.setAdapter(adapter);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.getTabAt(tab).select();

        //关联ViewPager
        //tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
    @Override
    public void event(String type){
        switch(type){
            case"unOrder":
                user.addOrderFoodList(user.getUnOrderList());
                user.clearUnOrderList();
                break;
            case"ordered":
                user.clearOrderList();
                Toast.makeText(getApplicationContext(),"您好，老顾客，本次你可享受 7 折优惠",Toast.LENGTH_SHORT).show();
                break;
        }
        //adapter = new OrderViewAdapter(getSupportFragmentManager(),fragments1);
        //viewPager.setAdapter(adapter);
        FragmentManager fgManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
        fragmentTransaction.remove(myOrderFragment);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.getTabAt(1).select();
    }

    public void event_cancel(Data data) {
        user.deletUnOrderFood(data);
        FragmentManager fgManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
        fragmentTransaction.remove(myOrderFragment);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        //adapter = new OrderViewAdapter(getSupportFragmentManager(),fragments1);
        //viewPager.setAdapter(adapter);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
        @Override
        public Fragment getItem(int position) {
            myOrderFragment = new MyOrderFragment();
            if (list.get(position).equals(titles[0])){
                myOrderFragment.setFragmentType("unOrder");
                myOrderFragment.setUser(user);
            }else if (list.get(position).equals(titles[1])){
                myOrderFragment.setFragmentType("ordered");
                myOrderFragment.setUser(user);
            }
            myOrderFragment.addUnOrder(user.getUnOrderList());
            myOrderFragment.addOrder(user.getOrderList());
            return myOrderFragment;
        }
        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
