package com.bb.offerapp.fragment.viewpaper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by bb on 2017/9/14.这种方式是实现了耗时操作的懒加载，并不是真正的懒加载所有
 */

public abstract class MyFragment extends Fragment{

    /** Fragment当前状态是否可见 */
    protected boolean isUIVisible;

    protected boolean isViewCreated;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated=true;
        lazyLoad();
    }

    private void lazyLoad(){
        if(isUIVisible==true&&isViewCreated==true){
            loadData();
            isUIVisible=false;
            isViewCreated=false;//防止重复懒加载 默认当前页前后2页不会重复加载，除非设置了viewpager缓存数
        }
    }


    /* 延迟加载
         * 子类必须重写此方法
         */
    protected abstract void loadData();
}


