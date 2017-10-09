package com.greason.smoothinput;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greason on 2017/9/25.
 */
public class ListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView testaa = (TextView) findViewById(R.id.testaa);
        testaa.setText("123456");

        listView = (ListView) findViewById(R.id.scroll);
        SmoothCreate.create(listView, (View) listView.getParent());

        List<String> data = new ArrayList<>();
        data.add("greason");
        listView.setAdapter(new ArrayAdapter(this, R.layout.item, R.id.test, data));

        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        EditText input = (EditText) view.findViewById(R.id.input);
        input.setHint("哈哈1");

        listView.addHeaderView(view);

    }

}
