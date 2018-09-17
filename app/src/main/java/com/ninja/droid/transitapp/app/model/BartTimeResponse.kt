package com.ninja.droid.transitapp.app.model

import com.google.gson.annotations.SerializedName

data class BartTrainTimeResponse(val root: ResponseRoot)
data class ResponseRoot(val schedule: ResponseSchedule)
data class ResponseSchedule(val request: ResponseScheduleRequest)
data class ResponseScheduleRequest(val trip: List<ResponseTrip>)
data class ResponseTrip(@SerializedName("@origTimeMin") val departureTime: String)
