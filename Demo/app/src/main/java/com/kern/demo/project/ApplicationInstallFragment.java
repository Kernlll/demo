package com.kern.demo.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.kern.demo.R;

import java.io.File;
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
public class ApplicationInstallFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.choose_apk)
    Button chooseApk;
    @BindView(R.id.result1)
    TextView result1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_install, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chooseApk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        //intent.setType("video/mp4");//仅仅mp4

        intent.setType("application/vnd.android.package-archive");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = null;
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    path = uri.getPath();
                    Log.i("wangke", "三方应用打开");
                    result1.append(path+"\n");
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    Log.i("wangke", "after 4.4");
                    path = getPath(getActivity(), uri);
                    result1.append(path+"\n");
                } else {//4.4以下下系统调用方法
                    Log.i("wangke", "before 4.4");
                    path = getRealPathFromURI(uri);
                    result1.append(path+"\n");
                }
                goInstall(path);
            } else {
                result1.append("获取路径失败\n");
            }
        }
    }

    private void goInstall(String path) {
        Log.i("wangke","go install");
        boolean isGranted = getActivity().getPackageManager().canRequestPackageInstalls();
        Log.i("wangke","install permission granted:"+isGranted);
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(getActivity(), "com.kern.demo.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

        }else{
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        Log.i("wangke","startActivity:"+file.getAbsolutePath());
        startActivity(intent);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {


                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));


                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];


                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }


                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};


                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {


        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};


        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void monitorInput(final String text) {
        //Log.i("wangke", text);
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //Log.d("wangke", "subscribe:" + Thread.currentThread().getName());
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
                    StringBuffer buffer = new StringBuffer();
                    for (PackageInfo info : packageInfos) {
                        String name = info.applicationInfo.loadLabel(pm).toString();
                        // Log.i("wangke", "应用名称：" + name);
                        if (text.equals(name)) {
                            Log.i("wangke", "get app");
                            buffer.append("\n" + "应用名称：" + name + "\n");
                            buffer.append("应用包名：" + info.packageName + "\n");
                            buffer.append("应用版本：" + info.versionName + "\n");
                            buffer.append("应用版本号：" + info.versionCode + "\n");
                        }
                    }
                    if (TextUtils.isEmpty(buffer.toString())) {
                        try {
                            PackageInfo info = pm.getPackageInfo(text, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
                            if (info != null) {
                                //Log.i("wangke", "get app");
                                String name = info.applicationInfo.loadLabel(pm).toString();
                                buffer.append("\n" + "应用名称：" + name + "\n");
                                buffer.append("应用包名：" + info.packageName + "\n");
                                buffer.append("应用版本：" + info.versionName + "\n");
                                buffer.append("应用版本号：" + info.versionCode + "\n");
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
                        editor.putString("app_install_name", text);
                        editor.apply();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //Log.d("wangke", "accept:" + s + ":" + Thread.currentThread().getName());
                result1.setText(s);
            }
        };
//关注点
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

}
