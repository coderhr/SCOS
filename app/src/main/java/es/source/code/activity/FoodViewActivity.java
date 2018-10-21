package es.source.code.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import es.source.code.fragment.MyFragment;
import es.source.code.model.Data;
import es.source.code.model.User;

public class FoodViewActivity extends AppCompatActivity implements MyFragment.CallBack{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<String>list;

    //private ArrayList<MyFragment> fragments;
    private int f_pos = 0;
    ArrayList<Data> list1 = new ArrayList<>();
    ArrayList<Data> list2 = new ArrayList<>();
    ArrayList<Data> list3 = new ArrayList<>();
    ArrayList<Data> list4 = new ArrayList<>();

    MyFragment myFragment;
    User user;

    private String[] titles = {"冷菜", "热菜", "海鲜", "酒水"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        user = (User)getIntent().getSerializableExtra("User");
        f_pos = getIntent().getIntExtra("int", 0);

        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);
        list.add(titles[2]);
        list.add(titles[3]);

        MyFragment.setCallBack(this);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
        tabLayout.getTabAt(f_pos).select();

        list1 = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list1.add(new Data("菜名"+i,8*i, false));
        }

        list2 = new ArrayList<>();
        for(int i = 10; i < 20; i++) {
            list2.add(new Data("菜名"+i, 3*i, false));
        }

        list3 = new ArrayList<>();
        for(int i = 20; i < 30; i++ ) {
            list3.add(new Data("菜名"+i, 2*i, false));
        }
        for(int i = 30; i < 40; i++) {
            list4.add(new Data("菜名"+i, 1*i, false));
        }


      /*  //设置ViewPager的适配器
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);*/

        //关联ViewPager
        //  tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void event(Data data){
        if(data.isOrder() == true){
            user.addUnOrderFood(data);
        }else{
            user.deletUnOrderFood(data);
        }
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
            myFragment = new MyFragment();
            if (list.get(position).equals(titles[0])){
                myFragment.addData(list1);
            }else if (list.get(position).equals(titles[1])){
                myFragment.addData(list2);
            }else if (list.get(position).equals(titles[2])){
                myFragment.addData(list3);
            }else if (list.get(position).equals(titles[3])){
                myFragment.addData(list4);
            }
            myFragment.setUser(user);
            myFragment.setPosintion(position);
            return myFragment;
        }
        @Override
        public int getCount() {
            return titles.length;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {      //重写创建菜单的方法
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.food:
                intent.setClass(FoodViewActivity.this, FoodOrderViewActivity.class);
                intent.putExtra("String", "FromFoodView");
                intent.putExtra("User", user);
                intent.putExtra("tab", 0);
                startActivity(intent);
                break;
            case R.id.list:
                intent.setClass(FoodViewActivity.this, FoodOrderViewActivity.class);
                intent.putExtra("String", "FromFoodView");
                intent.putExtra("User", user);
                intent.putExtra("tab", 1);
                startActivity(intent);
                break;
            case R.id.server:
                break;
        }
        return true;
    }

}
