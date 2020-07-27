package com.kern.demo.project;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kern.demo.BaseActivity;
import com.kern.demo.R;
import com.kern.demo.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ApplicationActivity extends BaseActivity {

    private static final int NOT_NOTICE = 2;//如果勾选了不再询问

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    public static final String[] tabs = {"信息", "备份", "安装"};

    private List<Fragment> fragmentList;

    private ApplicationFragmentAdapter adapter;

    private AlertDialog mDialog;
    private AlertDialog alertDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_application;
    }

    @Override
    protected int getToolbarType() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentList = new ArrayList<>();
        fragmentList.add(new ApplicationInfoFragment());
        fragmentList.add(new ApplicationBackupFragment());
        fragmentList.add(new ApplicationInstallFragment());

        adapter = new ApplicationFragmentAdapter(getSupportFragmentManager(),fragmentList);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        setToolbarTitle("应用相关操作");
        myRequetPermission();
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                        Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {//用户选择了禁止不再询问

                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("permission")
                                    .setMessage("点击允许才可以使用我们的app哦")
                                    .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (mDialog != null && mDialog.isShowing()) {
                                                mDialog.dismiss();
                                            }
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                            intent.setData(uri);
                                            startActivityForResult(intent, NOT_NOTICE);
                                        }
                                    });
                            mDialog = builder.create();
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                        }else {//选择禁止
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("permission")
                                    .setMessage("点击允许才可以使用我们的app哦")
                                    .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (alertDialog != null && alertDialog.isShowing()) {
                                                alertDialog.dismiss();
                                            }
                                            ActivityCompat.requestPermissions(ApplicationActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                        }
                                    });
                            alertDialog = builder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE){
            myRequetPermission();//由于不知道是否选择了允许所以需要再次判断
        }
    }

    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }
}
