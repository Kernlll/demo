package com.kern.demo.frame.fragment;

import android.content.Intent;

import com.kern.demo.CategoryBaseFragment;
import com.kern.demo.R;
import com.kern.demo.frame.rxjava.RxCreateActivity;
import com.kern.demo.frame.rxjava.RxFlatMapActivity;
import com.kern.demo.frame.rxjava.RxMapActivity;
import com.kern.demo.frame.rxjava.RxZipActivity;
import com.kern.demo.mode.OperatorModel;

import java.util.ArrayList;

/**
 * @Author: wangke
 */
public class OperatorsFragment extends CategoryBaseFragment {
    @Override
    protected void fillData() {
        data = new ArrayList<>();
        data.add(new OperatorModel(getString(R.string.rx_create), "可用于获取一个被观察的对象"));
        data.add(new OperatorModel(getString(R.string.rx_zip), "合并事件专用," +
                "分别从两个上游事件中各取出一个组合," +
                "一个事件只能被使用一次，顺序严格按照事件发送的顺序," +
                "最终下游事件收到的是和上游事件最少的数目相同（必须两两配对，多余的舍弃)"));
        data.add(new OperatorModel(getString(R.string.rx_map), "基本是RxJava 最简单的操作符了作用是对上游发送的每一个事件应用一个函数，" +
                "使得每一个事件都按照指定的函数去变化"));
        data.add(new OperatorModel(getString(R.string.rx_flatMap), "FlatMap将一个发送事件的上游Observable变换成多个发送事件的Observables， 然后将它们发射的时间合并后放进一个单独的Observable里"));
        data.add(new OperatorModel(getString(R.string.rx_concatMap), "concatMap作用和flatMap几乎一模一样，唯一的区别是它能保证事件的顺序"));
        data.add(new OperatorModel(getString(R.string.rx_doOnNext), "让订阅者在接收到数据前干点事情的操作符"));
        data.add(new OperatorModel(getString(R.string.rx_filter), "过滤操作符，取正确的值"));
        data.add(new OperatorModel(getString(R.string.rx_skip), "接受一个long型参数，代表跳过多少个数目的事件再开始接收"));
        data.add(new OperatorModel(getString(R.string.rx_take), "用于指定订阅者最多收到多少数据"));
        data.add(new OperatorModel(getString(R.string.rx_timer), "在Rxjava中timer 操作符既可以延迟执行一段逻辑，也可以间隔执行一段逻辑\n" +
                "【注意】但在RxJava 2.x已经过时了，现在用interval操作符来间隔执行，详见RxIntervalActivity\n" +
                "timer和interval都默认执行在一个新线程上。"));
        data.add(new OperatorModel(getString(R.string.rx_interval), "间隔执行操作，默认在新线程"));
        data.add(new OperatorModel(getString(R.string.rx_just), "just操作符，和RxJava 1.x 没有什么区别，就是接受一个可变参数，依次发送"));
        data.add(new OperatorModel(getString(R.string.rx_single), "顾名思义，Single只会接收一个参数" +
                "而SingleObserver只会调用onError或者onSuccess"));
        data.add(new OperatorModel(getString(R.string.rx_concat), "连接操作符，可接受Observable的可变参数，或者Observable的集合"));
        data.add(new OperatorModel(getString(R.string.rx_distinct), "去重操作符，其实就是简单的去重"));
        data.add(new OperatorModel(getString(R.string.rx_buffer), "buffer(count, skip)` 从定义就差不多能看出作用了，将 observable 中的数据按 skip（步长）分成最长不超过 count 的 buffer，然后生成一个 observable"));
        data.add(new OperatorModel(getString(R.string.rx_debounce), "过滤掉发射速率过快的数据项"));
        data.add(new OperatorModel(getString(R.string.rx_defer), "就是在每次订阅的时候就会创建一个新的 Observable"));
        data.add(new OperatorModel(getString(R.string.rx_last), "取出最后一个值，参数是没有值的时候的默认值"));
        data.add(new OperatorModel(getString(R.string.rx_merge), "将多个Observable合起来，接受可变参数，也支持使用迭代器集合"));
        data.add(new OperatorModel(getString(R.string.rx_reduce), "就是一次用一个方法处理一个值，可以有一个seed作为初始值"));
        data.add(new OperatorModel(getString(R.string.rx_scan), "和上面的reduce差不多，区别在于reduce()只输出结果，而scan()会将过程中每一个结果输出"));
        data.add(new OperatorModel(getString(R.string.rx_window), "按照时间划分窗口，将数据发送给不同的Observable"));
        data.add(new OperatorModel(getString(R.string.rx_PublishSubject), "onNext() 会通知每个观察者，仅此而已"));
        data.add(new OperatorModel(getString(R.string.rx_AsyncSubject), "在调用 onComplete() 之前，除了 subscribe() 其它的操作都会被缓存，在调用 onComplete() 之后只有最后一个 onNext() 会生效"));
        data.add(new OperatorModel(getString(R.string.rx_BehaviorSubject), "BehaviorSubject 的最后一次 onNext() 操作会被缓存，然后在 subscribe() 后立刻推给新注册的 Observer"));
        data.add(new OperatorModel(getString(R.string.rx_Completable), "只关心结果，也就是说 Completable 是没有 onNext 的，要么成功要么出错，不关心过程，在 subscribe 后的某个时间点返回结果"));
        data.add(new OperatorModel(getString(R.string.rx_Flowable), "专用于解决背压问题"));
    }

    @Override
    protected void itemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), RxCreateActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), RxZipActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), RxMapActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), RxFlatMapActivity.class));
                break;
            case 4:
                break;
        }

    }
}
