package com.egdroid.gradientpolyline.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Ahmed Adel on 5/3/20.
 *
 * Working as Senior Software Engineer at Zendesk
 * Email: ahmed.adel.said@gmail.com
 * Website: https://about.me/ahmed.adel.said/
 */
@JsonClass(generateAdapter = true)
data class RouteDto(

    @Json(name = "coordinates")
    val coordinates: List<List<Double>>

)