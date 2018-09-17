package com.ninja.droid.transitapp.app.viewmodels

import com.ninja.droid.transitapp.app.network.TransitApi
import io.reactivex.Observable

class TransitViewModel {

    fun getBartTimes(): Observable<List<String>> {
        return TransitApi.fetchBartTimes()
    }

    fun getBusTimes(): Observable<List<String>> {
        return TransitApi.fetchBusTimes()

    }
}