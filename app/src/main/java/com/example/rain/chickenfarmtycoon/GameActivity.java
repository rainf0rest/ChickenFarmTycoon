package com.example.rain.chickenfarmtycoon;

import android.app.Activity;
import android.os.Bundle;
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

/**
 * Created by rain on 2016/10/15.
 */
public class GameActivity extends Activity {

    private TextView tipTextView;
    private ListView farmListView;
    private ArrayList<Chicken> chickens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farm);

        tipTextView = (TextView) findViewById(R.id.farm_tips_textview);
        tipTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        farmListView = (ListView) findViewById(R.id.farm_listView);

        chickens = new ArrayList<Chicken>();
        initChickenData();

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
