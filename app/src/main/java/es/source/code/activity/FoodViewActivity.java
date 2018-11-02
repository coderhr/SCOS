package es.source.code.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import es.source.code.fragment.MyFragment;
import es.source.code.model.Data;
import es.source.code.model.DataList;
import es.source.code.model.User;
import es.source.code.service.ServerObserverService;

public class FoodViewActivity extends AppCompatActivity implements MyFragment.CallBack{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<String>list;
    private boolean startService;
    private Messenger serverMessagnger;
    private IBinder iBinder;
    MyPagerAdapter myPagerAdapter;
    ServiceConnection connection;
    private Intent intentService;
    private int flag = 0;

    private int f_pos = 0;
    ArrayList<Data> list1 = new ArrayList<>();
    ArrayList<Data> list2 = new ArrayList<>();
    ArrayList<Data> list3 = new ArrayList<>();
    ArrayList<Data> list4 = new ArrayList<>();

    private ArrayList<MyFragment> fragments = new ArrayList<>();

    User user = new User();

    private String[] titles = {"冷菜", "热菜", "海鲜", "酒水"};

    private Handler sMessageHandler = new Handler(){

        @Override
        public void handleMessage(Message fromServer) {
            if (fromServer.what == 10) {
                DataList dataList = new DataList();
                if (fromServer.getData().getSerializable("coldFood") != null) {
                    list1 = new ArrayList((ArrayList<Data>) fromServer.getData().getSerializable("coldFood"));
                    dataList.setColdFood(list1);
                }
                if (fromServer.getData().getSerializable("hotFood") != null) {
                    list2 = new ArrayList((ArrayList<Data>) fromServer.getData().getSerializable("hotFood"));
                    dataList.setHotFood(list2);
                }
                if (fromServer.getData().getSerializable("seaFood") != null) {
                    list3 = new ArrayList((ArrayList<Data>) fromServer.getData().getSerializable("seaFood"));
                    dataList.setSeaFood(list3);
                }
                if (fromServer.getData().getSerializable("drinkFood") != null) {
                    list4 = new ArrayList((ArrayList<Data>) fromServer.getData().getSerializable("drinkFood"));
                    dataList.setDrinkFood(list4);
                }

                fragments.get(0).addData((ArrayList<Data>) list1);
                fragments.get(1).addData((ArrayList<Data>) list2);
                fragments.get(2).addData((ArrayList<Data>) list3);
                fragments.get(3).addData((ArrayList<Data>) list4);


                myPagerAdapter.remove();
                fragments.add(new MyFragment());
                fragments.add(new MyFragment());
                fragments.add(new MyFragment());
                fragments.add(new MyFragment());

                user.setDataList(dataList);              //传回数据

                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                viewPager.setAdapter(myPagerAdapter);
                tabLayout.setupWithViewPager(viewPager, true);
                tabLayout.getTabAt(f_pos).select();
            }
                super.handleMessage(fromServer);
            }
    };

    private Messenger messenger = new Messenger(sMessageHandler);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        user = (User)getIntent().getSerializableExtra("User");
        f_pos = getIntent().getIntExtra("int", 0);      //获得位置信息

        /*添加标题内容*/
        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);
        list.add(titles[2]);
        list.add(titles[3]);

        MyFragment.setCallBack(this);            //将foodViewActivity的信息传出

        fragments.add(new MyFragment());
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());

        /*设置ViewPage与Tablelayout,设置标题栏*/
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
        tabLayout.getTabAt(f_pos).select();

       for(int i = 1; i < 5; i++) {
            list1.add(new Data("菜名"+i,8*i,false, 1));
        }

        for(int i = 6; i < 10; i++) {
            list2.add(new Data("菜名"+i, 3*i, false, 1));
        }

        for(int i = 11; i < 15; i++ ) {
            list3.add(new Data("菜名"+i, 2*i, false, 1));
        }
        for(int i = 16; i < 20; i++) {
            list4.add(new Data("菜名"+i, 1*i, false, 1));
        }

    }

    private void bindRemoteService(){
        intentService = new Intent();
        intentService.setClassName(this,"es.source.code.service.ServerObserverService");
        startService(intentService);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serverMessagnger = new Messenger(service);
                Message msg = new Message();
                msg.what = 1;
                msg.replyTo = messenger;
                try{
                    serverMessagnger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                iBinder = null;
            }
        };
        bindService(intentService, connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(connection != null)
            unbindService(connection);
    }

    /*向FoodOrderView的未点菜品中传递菜品信息（添加或删除）*/
    @Override
    public void event(Data data){
        if(data.isOrder() == true){
            user.addUnOrderFood(data);
        }else{
            user.deletUnOrderFood(data);
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager fm;
        ArrayList<MyFragment> fragments;

        public MyPagerAdapter(FragmentManager fm, ArrayList<MyFragment>fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
        @Override
        public Fragment getItem(int position) {        //获得标题的位置信息
            if (list.get(position).equals(titles[0])){
                fragments.get(0).addData((ArrayList<Data>) list1);
                fragments.get(0).setUser(user);
                fragments.get(0).setPosintion(position);
            }else if (list.get(position).equals(titles[1])){
                fragments.get(1).addData((ArrayList<Data>) list2);
                fragments.get(1).setUser(user);
                fragments.get(1).setPosintion(position);
            }else if (list.get(position).equals(titles[2])){
                fragments.get(2).addData((ArrayList<Data>) list3);
                fragments.get(2).setUser(user);
                fragments.get(2).setPosintion(position);
            }else if (list.get(position).equals(titles[3])){
                fragments.get(3).addData((ArrayList<Data>) list4);
                fragments.get(3).setUser(user);
                fragments.get(3).setPosintion(position);
            }
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return titles.length;
        }

        public void remove(){
            fragments.clear();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {      //重写创建菜单的方法
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        if(startService){
            menu.findItem(R.id.update).setTitle("停止实时更新");
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {         //设置菜单栏信息
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.food:
                intent.setClass(FoodViewActivity.this, FoodOrderViewActivity.class);
                intent.putExtra("String", "FromFoodView");        //值的传递
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
            case R.id.update:
                startService = true;
                invalidateOptionsMenu();
                if(serverMessagnger == null){
                    bindRemoteService();
                }else{
                    Message msg = new Message();
                    msg.what = 1;
                    if(flag == 1){
                        msg.what = 0;
                    }
                    flag = 1;
                    msg.replyTo = messenger;
                    try{
                        serverMessagnger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return true;
    }

}
