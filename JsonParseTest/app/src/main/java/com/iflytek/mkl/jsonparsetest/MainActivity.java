package com.iflytek.mkl.jsonparsetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String json = "{\"age\":\"25\",\"name\":\"hi\",\"point\":[1,2,3,4]}";


    String jsonArray = "[{\"name\":\"x1\"},{\"name\":\"x2\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gsonTest(json);
        fastJsonTest(jsonArray);
    }

    private void gsonTest(String str){
        Gson gson = new Gson();
        Bean bean = gson.fromJson(str, Bean.class);
        Log.d(TAG, "gsonTest "+bean.getName()+" "+bean.getPoint().size());
        Log.d(TAG, gson.toJson(bean));
    }

    private void fastJsonTest(String str){
        Bean b1 = new Bean();
        b1.setName("x1");
        Bean b2 = new Bean();
        b2.setName("x2");
        List<Bean> lb = new ArrayList<>();
        lb.add(b1);
        lb.add(b2);
        Log.d(TAG, "fastJsonTest: "+JSON.toJSON(lb));

        List<Bean> bean = JSON.parseObject(str, new TypeReference<List<Bean>>(){});
        Log.d(TAG, "gsonTest "+bean.get(0).getName()+" "+bean.size());
        Log.d(TAG, JSON.toJSONString(bean));
    }
}
