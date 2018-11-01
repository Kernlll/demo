package demo.kernlll.com.demo.volley;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import demo.kernlll.com.demo.R;

/*
    适合数据量小的频率高的请求，不适合下载上传大文件
    Volley的网络请求线程池默认大小为4。意味着可以并发进行4个请求，大于4个，会排在队列中。

 */
public class VolleyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley);
    }

}
