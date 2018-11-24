package com.doglegs.core.base;

/**
 * author: Mai_Xiao_Peng
 * email  : Mai_Xiao_Peng@163.com
 * time  : 2017/4/20
 */

public interface IBasePresenter<T extends IBaseView> {

    void attachView(T view);

    void detachView();

}
