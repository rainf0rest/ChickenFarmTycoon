package com.example.rain.chickenfarmtycoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rain on 2016/10/15.
 */
public class GameActivity extends Activity {

    private TextView farmTipsTextView, farmDaysTextView, farmMoneyTextView;
    private ListView farmListView;
    private ArrayList<Chicken> chickens;
    private ArrayList<Egg> eggs;
    private Handler farmTimeHandler;
    private Timer timer;
    private TimerTask task;
    private int days, firstPlay;
    private double money;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AlertDialog.Builder builder;
    private int[] eggFlag;
    private ComputerThread computerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farm);

        farmTipsTextView = (TextView) findViewById(R.id.farm_tips_textview);
        farmTipsTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        farmDaysTextView = (TextView) findViewById(R.id.farm_days_textview);
        farmMoneyTextView = (TextView) findViewById(R.id.farm_money_textview);

        farmListView = (ListView) findViewById(R.id.farm_listView);

        chickens = new ArrayList<>();
        eggs = new ArrayList<>();

        computerThread = new ComputerThread();
        computerThread.start();

        chickens = new ArrayList<Chicken>();
        //initChickenData();

        //天数,金钱读档or初始化
        sharedPreferences = getSharedPreferences("Testdata", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        days = sharedPreferences.getInt("FarmTime", 0);
        money = sharedPreferences.getInt("FarmMoney", 100);
        firstPlay = sharedPreferences.getInt("firstPlay", 0);
        //days = 0;
        farmDaysTextView.setText("天数：" + days);
        farmMoneyTextView.setText("金币：" + money);

        //蛋与鸡  读档
        String eggsString = sharedPreferences.getString("eggs", null);
        String chickensString = sharedPreferences.getString("chickens", null);

        if(eggsString != null) {
            //egg read data
            try {
                try {
                    ArrayList<Egg> eg = deSerializationEggs(eggsString);
                    eggs = eg;
                }
                catch (ClassNotFoundException ex) {

                }

            }
            catch (IOException e) {

            }
        }
        if(chickensString != null) {
            //chicken read data
            try {
                try {
                    ArrayList<Chicken> eg = deSerializationChickens(chickensString);
                    chickens = eg;
                }
                catch (ClassNotFoundException ex) {

                }

            }
            catch (IOException e) {

            }
        }


        if(firstPlay == 0) {
            initData();
        }


        showListView();

        farmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(GameActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
                if(position < eggs.size()) {
                    showEggDia(position);
                }
                else{
                    showChickenDia(position);
                }

            }
        });




        /*
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return eggs.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {

                if(convertView == null) {
                    convertView = LayoutInflater.from(GameActivity.this).inflate(R.layout.farm_list_item, null);
                    Button button = (Button) convertView.findViewById(R.id.farm_chicken_operate);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(GameActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                convertView.setTag(R.id.farm_chicken_operate, position);
                return null;
            }
        };

        farmListView.setAdapter(baseAdapter);*/

        farmTimeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x001:
                        // do some action
                        days++;
                        farmDaysTextView.setText("天数：" + days);
                        editor.putInt("FarmTime", days);
                        editor.commit();
                        //refleshTips("");
                        showListView();
                        break;
                    default:
                        break;
                }
            }
        };

        //计时器
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0x101;
                //message.obj = System.currentTimeMillis();
                computerThread.myHandler.sendMessage(message);
            }
        };

        timer = new Timer();
        // 参数：
        // 1000，延时1秒后执行。
        // 2000，每隔2秒执行1次task。
        timer.schedule(task, 1000, 10000);
    }

    private void initData() {
        initChickenData1(1);
        initChickenData1(1);
        initChickenData1(1);
    }

    private void initChickenData1(int level) {
        Egg eg = new Egg(level);
        eggs.add(eg);
    }

    private void refleshTips(String s) {
        farmTipsTextView.append("\n" + s);
    }


    //序列化对象

    private String serializeEggs() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        //eggs
        objectOutputStream.writeObject(eggs);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    //序列化对象

    private String serializeChickens() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        //eggs
        objectOutputStream.writeObject(chickens);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    //反序列化对象

    private ArrayList<Egg> deSerializationEggs(String str) throws IOException,
            ClassNotFoundException {

        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);

        ArrayList<Egg> p = (ArrayList<Egg>) objectInputStream.readObject();

        objectInputStream.close();
        byteArrayInputStream.close();
        return p;
    }

    //反序列化对象

    private ArrayList<Chicken> deSerializationChickens(String str) throws IOException,
            ClassNotFoundException {

        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);

        ArrayList<Chicken> p = (ArrayList<Chicken>) objectInputStream.readObject();

        objectInputStream.close();
        byteArrayInputStream.close();
        return p;
    }

    private void showEggDia(final int postion) {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.eggs);
        builder.setTitle(R.string.operate);

        final String[] Items={"孵化","出售"};
        builder.setItems(Items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(), "You clicked "+Items[i], Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0:
                        eggs.get(postion).setBorn(true);
                        break;
                    case 1:
                        money = money + eggs.get(postion).getPrice();
                        eggs.remove(postion);
                        showFarmMoneyText();
                        showListView();
                }
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void showChickenDia(final int postion) {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.chickens);
        builder.setTitle(R.string.operate);

        final String[] Items={"育种","出售"};
        builder.setItems(Items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(), "You clicked "+Items[i], Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0:
                        //eggs.get(postion).setBorn(true);
                        break;
                    case 1:
                        int p;
                        if(eggs.isEmpty()) {
                            p = postion;
                        }
                        else{
                            p = postion - eggs.size();
                        }

                        money = money + chickens.get(p).getPrice();
                        chickens.remove(p);
                        showFarmMoneyText();
                        showListView();
                }
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void showFarmMoneyText() {
        farmMoneyTextView.setText("金币：" + money);
        //editor.putInt("FarmMoney", money);
        //editor.commit();
    }

    private void showListView() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < eggs.size(); i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("name", eggs.get(i).getName());
            listItem.put("detail", "" + eggs.get(i).getLevel());
            listItem.put("status", "孵化：" + eggs.get(i).getBornTime() + "天 / " + eggs.get(i).getBornTimeTop() + "天");
            listItems.add(listItem);
        }

        for(int i = 0; i < chickens.size(); i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("name", chickens.get(i).getName());
            listItem.put("detail", "" + chickens.get(i).getLevel());
            listItem.put("status", "体重：" + chickens.get(i).getWeight() + "g\n价格：" + chickens.get(i).getPrice() + " 金币");
            listItems.add(listItem);
        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                listItems,
                R.layout.farm_list_item,
                new String[] {"name", "detail", "status"},
                new int[] {R.id.farm_chicken_name, R.id.farm_chicken_tips, R.id.farm_chicken_status});

        farmListView.setAdapter(simpleAdapter);
    }

    class ComputerThread extends Thread {
        public Handler myHandler;

        public void run() {
            Looper.prepare();
            myHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0x101) {
                        //textView.setText(msg.getData().getString("data"));
                        //Toast.makeText(WorkActivity.this, "Temperature get success!", Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < eggs.size(); i++) {
                            if(eggs.get(i).isBorn() == true) {
                                if(eggs.get(i).decBornTime()) {
                                    chickens.add(eggs.get(i).born());
                                    eggs.remove(i);
                                    i--;
                                }
                            }
                        }
                        for(int i = 0; i < chickens.size(); i++) {
                            chickens.get(i).growWeight();
                        }
                    }
                    Message message = new Message();
                    message.what = 0x001;
                    farmTimeHandler.sendMessage(message);
                }
            };
            Looper.loop();
        }
    }//0x124

}
