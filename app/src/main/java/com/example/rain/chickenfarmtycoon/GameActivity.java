package com.example.rain.chickenfarmtycoon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

    private TextView tipTextView, farmDaysTextView;
    private ListView farmListView;
    private ArrayList<Chicken> chickens;
    private Handler farmTimeHandler;
    private Timer timer;
    private TimerTask task;
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farm);

        tipTextView = (TextView) findViewById(R.id.farm_tips_textview);
        tipTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        farmDaysTextView = (TextView) findViewById(R.id.farm_days_textview);

        farmListView = (ListView) findViewById(R.id.farm_listView);

        chickens = new ArrayList<Chicken>();
        initChickenData();

        //天数读档or初始化
        days = 0;
        farmDaysTextView.setText("天数：" + days);

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < chickens.size(); i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("name", chickens.get(i).getName());
            listItem.put("detail", chickens.get(i).getNumber());
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                listItems,
                R.layout.farm_list_item,
                new String[] {"name", "detail"},
                new int[] {R.id.farm_chicken_name, R.id.farm_chicken_tips});

        farmListView.setAdapter(simpleAdapter);

        farmTimeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x001:
                        // do some action
                        days++;
                        farmDaysTextView.setText("天数：" + days);
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
                message.what = 0x001;
                //message.obj = System.currentTimeMillis();
                farmTimeHandler.sendMessage(message);
            }
        };

        timer = new Timer();
        // 参数：
        // 1000，延时1秒后执行。
        // 2000，每隔2秒执行1次task。
        timer.schedule(task, 1000, 10000);
    }

    private void initChickenData() {
        initChickenData1(1, 100);
        initChickenData1(2, 30);
        initChickenData1(3, 10);
    }

    private void initChickenData1(int kind, int num) {
        Chicken chicken = new Chicken();
        chicken.setKind(kind);
        chicken.makeName();
        chicken.setNumber(num);
        //tipTextView.append("\n" + chicken.getName() + " num:" + chicken.getNumber());
        chickens.add(chicken);
    }

}
