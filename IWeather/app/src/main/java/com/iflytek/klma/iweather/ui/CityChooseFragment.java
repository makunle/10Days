package com.iflytek.klma.iweather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iflytek.klma.iweather.R;

import static com.iflytek.klma.iweather.R.id.btn_search;

/**
 * 天气选择界面，包括
 * 1、文字输入选择
 * 2、热门城市列表点选
 * 3、语音搜索
 */

public class CityChooseFragment extends Fragment {


    private EditText etCityInput;    //城市查询输入框
    private Button btnSearch;        //城市搜索按钮
    private Button btnSpeechSearch;  //语音搜索按钮
    private RecyclerView cityList;   //城市列表

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_choose, container, false);
        etCityInput = (EditText) view.findViewById(R.id.et_city_input);
        btnSearch = (Button) view.findViewById(btn_search);
        btnSpeechSearch = (Button) view.findViewById(R.id.btn_speech_search);
        cityList = (RecyclerView) view.findViewById(R.id.rv_city_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
