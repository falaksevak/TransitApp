package com.ninja.droid.transitapp.app.network

import com.ninja.droid.transitapp.app.model.BartTrainTimeResponse
import com.ninja.droid.transitapp.app.model.BusTimeResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object TransitApi {

    private val BART_API_KEY = "MW9S-E7SL-26DU-VV8V"
    private val AC_TRANSIT_API_KEY = "2AE9E5DBE45337250F5EA0A568650A81"

    private val BART_BASE_URL = "https://api.bart.gov/api/"
    private val AC_TRANSIT_BASE_URL = "https://api.actransit.org/transit/"

    private var bartApi: BartService
    private var busApi: BusService

    init {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
        val gsonConverterFactory = GsonConverterFactory.create()
        val bartRetrofitBuilder = Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .client(httpClient.build())
                .baseUrl(BART_BASE_URL)
                .build()

        val busRetrofitBuilder = Retrofit.Builder()
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(AC_TRANSIT_BASE_URL)
                .client(httpClient.build())
                .build()

        bartApi = bartRetrofitBuilder.create(BartService::class.java)
        busApi = busRetrofitBuilder.create(BusService::class.java)
    }

    interface BartService {

        @GET("sched.aspx?cmd=depart&json=y&key=MW9S-E7SL-26DU-VV8V")
        fun getNextTrainTime(@Query("orig") origin: String,
                             @Query("dest") destination: String):
                Observable<BartTrainTimeResponse>
    }

    interface BusService {

        @GET("route/{routeName}/tripestimate?token=2AE9E5DBE45337250F5EA0A568650A81")
        fun getNextBusTime(@Path("routeName") routeName: String,
                           @Query("fromStopId") fromStopId: String,
                           @Query("toStopId") toStopId: String): Observable<BusTimeResponse>
    }

    fun fetchBartTimes(): Observable<List<String>> {
        return bartApi.getNextTrainTime("ftvl", "mont")
                .map { response ->
                    listOf(
                            response.root.schedule.request.trip[2].departureTime,
                            response.root.schedule.request.trip[3].departureTime
                    )
                }
    }

    fun fetchBusTimes(): Observable<List<String>> {
        return busApi.getNextBusTime("NX3", "54557", "50018")
                .map { response -> listOf<String>() }


    }
}
