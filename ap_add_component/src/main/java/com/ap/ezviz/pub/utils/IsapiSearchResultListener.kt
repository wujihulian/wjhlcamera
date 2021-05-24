package com.ap.ezviz.pub.utils


/**
 * 搜索结果
 * 配置网络的通用对话框
 * Created by zhangshuailing on 2018/7/18.
 */
interface IsapiSearchResultListener {
    /**
     * 搜索设备成功
     */
    fun onSearchSuccess(result: IsapiSearchHelper.ResultBean)

    /**
     * 搜所超时，即搜索失败
     */
    fun onSearchTimeOut()

    fun isCancel():Boolean


    /**
     * 重试
     */
    fun retry()
}