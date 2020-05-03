package com.egdroid.gradientpolyline.repo

import android.content.Context
import com.egdroid.gradientpolyline.model.RouteDto
import com.egdroid.gradientpolyline.model.RouteUiModel
import com.egdroid.gradientpolyline.utils.getJsonDataFromAsset
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Created by Ahmed Adel on 5/3/20.
 *
 * Working as Senior Software Engineer at Zendesk
 * Email: ahmed.adel.said@gmail.com
 * Website: https://about.me/ahmed.adel.said/
 */
class DefaultRouteRepo(
    private val dispatcher: CoroutineDispatcher
) : RouteRepo {

    private var moshi: Moshi = Moshi.Builder().build()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRoute(
        context: Context
    ): RouteUiModel = withContext(dispatcher) {

        val coordinateList = ArrayList<LatLng>()

        getJsonDataFromAsset(
            context = context,
            fileName = "route.json"
        )?.let { routeJsonString ->

            val jsonAdapter = moshi.adapter(RouteDto::class.java)
            jsonAdapter.fromJson(routeJsonString)?.let { coordinates ->

                coordinates.coordinates.forEach { routes ->
                    coordinateList.add(LatLng(routes[0], routes[1]))
                }

            }

        }

        RouteUiModel(coordinates = coordinateList)

    }

}

interface RouteRepo {
    suspend fun getRoute(context: Context): RouteUiModel
}