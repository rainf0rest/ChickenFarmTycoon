package com.example.rain.chickenfarmtycoon;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    private AlertDialog.Builder builder;
    private Button alertbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        alertbtn = (Button) findViewById(R.id.diabtn);

        alertbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.diabtn:
                showDiaList(view);
                break;
            default:
                break;
        }
    }

    void showDiaList(View view) {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.test);
        builder.setTitle("just Test");

        final String items[] = {
                "公鸡",
                "母鸡",
                "小鸡",
                "鸡蛋"
        };

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "You clicked "+ items[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();


    }

}
