package com.ap.ezviz.pub.utils

import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import com.ap.ezviz.pub.ap.FIXED_IP
import com.ap.ezviz.pub.http.APHttpClient
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * 通过ISAPI 配网 查询设备状态
 * Created by yexiaowei on 2019/6/12.
 */
class IsapiSearchHelper {

    companion object{
        private const val TAG = "IsapiSearchHelper"
        fun create():IsapiSearchHelper{
            return IsapiSearchHelper()
        }
    }

    private val handler = Handler()
    private var needCheckServerState = false
    /**
     * 在规定的时间内搜索设备
     * @param fixedIp 固定ip
     * @param repeatCount 重试次数
     */
    fun getActivateState(fixedIp: FIXED_IP, needCheckServerState:Boolean, listener: IsapiSearchResultListener, repeatCount:Int = 5){
        getActivateState(fixedIp.getIpPorts(), needCheckServerState, listener, repeatCount)
    }
    /**
     * 在规定的时间内搜索设备
     * @param ips 可能的ip地址，兼容设备可能存在多种ip的情况
     * @param repeatCount 重试次数
     */
    fun getActivateState(ips: ArrayList<FIXED_IP.IP_PORT>, needCheckServerState:Boolean, listener: IsapiSearchResultListener, repeatCount:Int = 5){
        this.needCheckServerState = needCheckServerState
        val startTime = SystemClock.elapsedRealtime()
        val atomicInteger = AtomicInteger(repeatCount)
        val tasks = ips.map {
            getActivateStateObservable(it)
        }
        excute(tasks, listener, atomicInteger, startTime)
    }

    fun doRetry(tasks: List<Observable<ResultBean?>>, listener: IsapiSearchResultListener, atomicInteger:AtomicInteger, startTime:Long){
        if (listener.isCancel()){
            atomicInteger.set(0)
        }
        val dtTime = SystemClock.elapsedRealtime() - startTime
        if (dtTime > 45*1000){ // 超过45秒超时
            atomicInteger.set(0)
        }
        if (atomicInteger.decrementAndGet() > 0){
            listener.retry()
            if (dtTime > 10*1000){
                // 立即执行
                excute(tasks, listener, atomicInteger, startTime)
            } else{
                // 等待3秒执行
                handler.postDelayed({
                    excute(tasks, listener, atomicInteger, startTime)
                }, 3000)
            }
        } else {
            listener.onSearchTimeOut()
        }
    }

    /**
     * 执行查询操作
     */
    private fun excute(tasks: List<Observable<ResultBean?>>, listener: IsapiSearchResultListener, atomicInteger:AtomicInteger, startTime:Long){

        val countDownLatch = CountDownLatch(tasks.size)
        var isSuccess = false
        tasks.forEach {
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object: Subscriber<ResultBean>() {
                        override fun onNext(result: ResultBean?) {
                            if (result != null){
                                countDownLatch.countDown()
                                isSuccess = true
                                executeCapabilities(result, listener)
                            } else {
                                onError(null)
                            }
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                            countDownLatch.countDown()
                        }
                    })
        }

        // 全部执行完成后
        Observable.fromCallable {
            countDownLatch.await()
            isSuccess
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<Boolean>() {
                    override fun onNext(result: Boolean) {
                        if (!result){
                            onError(null)
                        }
                    }
                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        doRetry(tasks, listener, atomicInteger, startTime)
                    }
                })
    }

    /**
     * 执行获取免摘要能力
     */
    private fun executeCapabilities(result: ResultBean, listener: IsapiSearchResultListener){
        if (!needCheckServerState){ // 萤石不需要检查服务状态
            result.enableHCPlatform = true // 新接口默认支持
            result.isOpenHCPlatform = true // 萤石默认开启
            listener.onSearchSuccess(result)
        } else {
            val observableCapabilities = getCapabilitiesObservable(result.fixedIp!!)
            observableCapabilities.map {
                result.isOpenHCPlatform = it
                result.enableHCPlatform = true // 新接口默认支持
                result
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<ResultBean>() {
                    override fun onNext(result: ResultBean?) {
                        if (result != null){
                            listener.onSearchSuccess(result)
                        } else {
                            onError(null)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        listener.onSearchTimeOut()
                    }
                })
        }
    }

    /**
     * 获取激活状态逻辑
     */
    private fun getActivateStateObservable(fixedIp: FIXED_IP.IP_PORT):Observable<ResultBean?>{
        return Observable.create { subscriber ->
            try {
                val response = APHttpClient.getActivateState(fixedIp)
                val content = response?.body()?.string()
                LogUtil.d(TAG, "configWifi() Response:$content")
                val activated = XmlUtils.getStringValue(content!!,"Activated")
                val resultBean = ResultBean()
                resultBean.fixedIp = fixedIp
                if (!TextUtils.isEmpty(activated)) {
                    resultBean.isActivated = activated == "true"
                    subscriber.onNext(resultBean)
                } else {
                    subscriber.onNext(null)
                }
            }catch (e:Exception){
                subscriber.onNext(null)
            }
            subscriber.onCompleted()
        }
    }

    /**
     * 获取免摘要能力逻辑
     */
    private fun getCapabilitiesObservable(fixedIp: FIXED_IP.IP_PORT):Observable<Boolean?>{
        return Observable.create { subscriber ->
            try {
                val response = APHttpClient.getCapabilities(fixedIp)
                val content = response?.body()?.string()
                LogUtil.d(TAG, "configWifi() Response:$content")
                val ezvizEnabled = XmlUtils.getStringValue(content!!,"EZVIZEnabled")
                subscriber.onNext(ezvizEnabled == "true")
            }catch (e:Exception){
                subscriber.onNext(null)
            }
            subscriber.onCompleted()
        }
    }

    class ResultBean{
        var fixedIp:FIXED_IP.IP_PORT? = null
        var isActivated:Boolean? = null
        var isOpenHCPlatform:Boolean? = null
        var enableHCPlatform:Boolean? = null
    }
}