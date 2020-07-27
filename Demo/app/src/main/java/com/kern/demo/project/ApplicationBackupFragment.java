package com.kern.demo.project;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kern.demo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: wangke
 */
public class ApplicationBackupFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ApplicationBackupFragment.class.getSimpleName();

    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.backup1)
    Button backup1;
    @BindView(R.id.result1)
    TextView result1;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.app_name)
    EditText appName;
    @BindView(R.id.backup2)
    Button backup2;
    @BindView(R.id.result2)
    TextView result2;

    Unbinder unbinder;
    @BindView(R.id.clear)
    Button clear;
    private static boolean isClear = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_backup, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backup1.setOnClickListener(this);
        backup2.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backup1:
                doBackupAll();
                break;
            case R.id.backup2:
                doBackupSingle();
                break;
            case R.id.clear:
                doClear();
                break;
        }
    }

    private void doClear() {
        isClear = true;
        Completable.create(new CompletableOnSubscribe(){

            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                File file = new File(getBackupPath());
                if (file.exists()) {
                    for (File file1:file.listFiles()) {
                        file1.delete();
                    }
                    file.delete();
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                result1.setText("数据清空");
            }

            @Override
            public void onError(Throwable e) {
                result1.setText("clear operation error："+e.getMessage());
            }
        });
    }

    Disposable mDisposable = null;
    private void doBackupAll() {
        //backupImpl(true);
        //oast.makeText(getActivity(),"全部备份完成",Toast.LENGTH_SHORT).show();
        result1.setText("");
        result1.append("\n");
        if (isClear) {
            isClear = false;
        }

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
                PackageManager packageManager = getActivity().getPackageManager();
                List<PackageInfo> allPackages = packageManager.getInstalledPackages(0);
                for (int i = 0; i < allPackages.size(); i++) {
                    PackageInfo packageInfo = allPackages.get(i);
                    String path = packageInfo.applicationInfo.sourceDir;
                    String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    Log.i(TAG, "path:" + path + ",name:" + name);
                    try {
/*                        if (isBackupSystemApp) {

                        } else {
                            if (isUserApp(packageInfo)) {
                                Log.i(TAG, name + " is not user app, skip...");
                                continue;
                            }
                        }*/
                        Log.i(TAG, "============================================");
                        Log.i(TAG, "backup out name:" + name);
                        File in = new File(path);
                        File mBaseFile = new File(getBackupPath());
                        if (!mBaseFile.exists()) {
                            mBaseFile.mkdir();
                        }
                        File out = new File(mBaseFile, name + ".apk");
                        if (!out.exists()) {
                            out.createNewFile();
                        }
                        FileInputStream fis = new FileInputStream(in);
                        FileOutputStream fos = new FileOutputStream(out,false);

                        int count;
                        byte[] buffer = new byte[256 * 1024];
                        while ((count = fis.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }

                        fis.close();
                        fos.flush();
                        fos.close();
                    } catch (Exception exception) {
                        Log.i(TAG, path + " Failed backup " + exception.getMessage());
                        exception.printStackTrace();
                        e.onError(exception);
                        continue;
                    }
                    e.onNext(path + "/" + name + ".apk");
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe : " + d.isDisposed() + "\n");
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                        Log.e(TAG, "onNext : value : " + s + "\n");
                        //备份期间点击了清空按钮，暂停接收事件
                        if (isClear) {
                            mDisposable.dispose();
                        }
                        isClear = false;
                        result1.append("backup:" + s + "\n");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError : value : " + e.getMessage() + "\n");
                        result1.append("onError:" + e.getMessage() + "\n");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete" + "\n");
                        result1.append("backup completed " + "\n");
                    }
                });
    }

    private void doBackupSingle() {
        final String s = appName.getText().toString();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(getActivity(),"请输入您想备份的应用名称或者包名",Toast.LENGTH_SHORT).show();
            return;
        }

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
                PackageManager packageManager = getActivity().getPackageManager();
                List<PackageInfo> allPackages = packageManager.getInstalledPackages(0);
                for (int i = 0; i < allPackages.size(); i++) {
                    PackageInfo packageInfo = allPackages.get(i);
                    String path = packageInfo.applicationInfo.sourceDir;
                    String packageName = packageInfo.packageName;
                    String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    Log.i(TAG, "path:" + path + ",name:" + name);
                    try {
                        if (!s.equalsIgnoreCase(packageName) && !s.equalsIgnoreCase(name)) {
                            continue;
                        }
                        Log.i(TAG, "============================================");
                        Log.i(TAG, "backup out name:" + name);
                        File in = new File(path);
                        File mBaseFile = new File(getBackupPath());
                        if (!mBaseFile.exists()) {
                            mBaseFile.mkdir();
                        }
                        File out = new File(mBaseFile, name + ".apk");
                        if (!out.exists()) {
                            out.createNewFile();
                        }
                        FileInputStream fis = new FileInputStream(in);
                        FileOutputStream fos = new FileOutputStream(out,false);

                        int count;
                        byte[] buffer = new byte[256 * 1024];
                        while ((count = fis.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }

                        fis.close();
                        fos.flush();
                        fos.close();
                        e.onNext(path + "/" + name + ".apk");
                    } catch (Exception exception) {
                        Log.i(TAG, path + " Failed backup " + exception.getMessage());
                        exception.printStackTrace();
                        e.onError(exception);
                        continue;
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe : " + d.isDisposed() + "\n");
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                        Log.e(TAG, "onNext : value : " + s + "\n");
                        //备份期间点击了清空按钮，暂停接收事件
                        if (isClear) {
                            mDisposable.dispose();
                        }
                        isClear = false;
                        result1.append("backup:" + s + "\n");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError : value : " + e.getMessage() + "\n");
                        result1.append("onError:" + e.getMessage() + "\n");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete" + "\n");
                        result1.append("backup completed " + "\n");
                    }
                });

    }

    public void backupImpl(boolean isBackupSystemApp) {
        //backup all user app
        PackageManager packageManager = getActivity().getPackageManager();
        List<PackageInfo> allPackages = packageManager.getInstalledPackages(0);
        for (int i = 0; i < allPackages.size(); i++) {
            PackageInfo packageInfo = allPackages.get(i);
            String path = packageInfo.applicationInfo.sourceDir;
            String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            Log.i(TAG, "path:" + path + ",name:" + name);
            try {
                if (isBackupSystemApp) {

                } else {
                    if (isUserApp(packageInfo)) {
                        Log.i(TAG, name + " is not user app, skip...");
                        continue;
                    }
                }
                backupApp(path, name);
            } catch (Exception e) {
                Log.i(TAG, path + " Failed backup " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
    }

    private void backupApp(String path, String outname) throws IOException {
        Log.i(TAG, "============================================");
        Log.i(TAG, "backup out name:" + outname);
        File in = new File(path);
        File mBaseFile = new File(getBackupPath());
        if (!mBaseFile.exists()) {
            mBaseFile.mkdir();
        }
        File out = new File(mBaseFile, outname + ".apk");
        if (!out.exists()) {
            out.createNewFile();
        }
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);

        int count;
        byte[] buffer = new byte[256 * 1024];
        while ((count = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }

        fis.close();
        fos.flush();
        fos.close();
    }

    public boolean isUserApp(PackageInfo pInfo) {
        return (((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                && ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0));
    }

    public String getBackupPath() {
        String path = getActivity().getExternalCacheDir() + File.separator + "ApkBackup" + File.separator;
        return path;
    }

    public void clear() {
        File mBaseFile = new File(getBackupPath());
        if (!mBaseFile.exists()) {
            return;
        }
        String[] list = mBaseFile.list();
        if (list == null) {
            return;

        }
        for (String s : list) {
            Log.i(TAG, "clear:" + s);
            File file = new File(getBackupPath() + File.separator + list);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
