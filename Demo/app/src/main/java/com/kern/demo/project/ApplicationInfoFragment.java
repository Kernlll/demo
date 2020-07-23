package com.kern.demo.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kern.demo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: wangke
 */
public class ApplicationInfoFragment extends Fragment {
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.installed_package)
    EditText installedPackage;
    @BindView(R.id.result1)
    TextView result1;
    @BindView(R.id.title2)
    Button title2;
    @BindView(R.id.no_installed_package)
    EditText noInstalledPackage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        installedPackage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                monitorInput(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SharedPreferences sp = getActivity().getSharedPreferences("app",Context.MODE_PRIVATE);
        String text = sp.getString("app_install_name","");
        installedPackage.setText(text);
        installedPackage.setSelection(text.length());


    }

    private void monitorInput(final String text) {
        Log.i("wangke",text);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d("wangke", "subscribe:" + Thread.currentThread().getName());
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
                    StringBuffer buffer = new StringBuffer();
                    for (PackageInfo info : packageInfos) {
                        String name = info.applicationInfo.loadLabel(pm).toString();
                        Log.i("wangke","应用名称："+name);
                        if (text.equals(name)) {
                            Log.i("wangke","get app");
                            buffer.append("\n"+"应用名称："+name+"\n");
                            buffer.append("应用包名："+info.packageName+"\n");
                            buffer.append("应用版本："+info.versionName+"\n");
                            buffer.append("应用版本号："+info.versionCode+"\n" );
                        }
                    }
                    if (TextUtils.isEmpty(buffer.toString())){
                        try {
                            PackageInfo info = pm.getPackageInfo(text,PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
                            if (info != null) {
                                Log.i("wangke","get app");
                                String name = info.applicationInfo.loadLabel(pm).toString();
                                buffer.append("\n"+"应用名称："+name+"\n");
                                buffer.append("应用包名："+info.packageName+"\n");
                                buffer.append("应用版本："+info.versionName+"\n");
                                buffer.append("应用版本号："+info.versionCode+"\n" );
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    if (TextUtils.isEmpty(buffer.toString())) {
                        emitter.onNext("未找到相关信息");
                    } else {
                        emitter.onNext(buffer.toString());
                        SharedPreferences sp = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("app_install_name",text);
                        editor.apply();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();;
                }

            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("wangke", "accept:" +s+":"+ Thread.currentThread().getName());
                result1.setText(s);
            }
        };
//关注点
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

}
