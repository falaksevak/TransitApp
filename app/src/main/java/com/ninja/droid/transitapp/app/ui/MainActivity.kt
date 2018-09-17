package com.ninja.droid.transitapp.app.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.ninja.droid.transitapp.app.R
import com.ninja.droid.transitapp.app.viewmodels.TransitViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : Activity() {
    private lateinit var disposable: DisposableObserver<List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBartButtonClick(view: View) {
        disposable = TransitViewModel().getBartTimes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<List<String>>() {
                    override fun onNext(times: List<String>) {
                        println(times.toString())
                    }

                    override fun onError(t: Throwable) {
                        println(t.localizedMessage)
                    }

                    override fun onComplete() {}

                })
    }

    fun onBusButtonClick(view: View) {
        disposable = TransitViewModel().getBusTimes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<List<String>>() {
                    override fun onNext(value: List<String>?) {

                    }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable?) {

                    }
                })
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}