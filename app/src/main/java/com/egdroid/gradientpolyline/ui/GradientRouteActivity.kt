package com.egdroid.gradientpolyline.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.egdroid.gradientpolyline.GradientPolyLine
import com.egdroid.gradientpolyline.R
import com.egdroid.gradientpolyline.ext.zoomCameraWithAnimation
import com.egdroid.gradientpolyline.model.RouteUiModel
import com.egdroid.gradientpolyline.viewmodel.GradientRouteVM
import com.egdroid.gradientpolyline.viewmodel.GradientRouteVMFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ahmed Adel on 5/3/20.
 *
 * Working as Senior Software Engineer at Zendesk
 * Email: ahmed.adel.said@gmail.com
 * Website: https://about.me/ahmed.adel.said/
 */
class GradientRouteActivity : AppCompatActivity(), CoroutineScope {

    /**
     * job which is used as a parent for all launched coroutines within the activity and
     * they will be cancelled in [onDestroy].
     */
    private val job = SupervisorJob()

    /**
     * Obtain [GradientRouteVM] by accessing its [GradientRouteVMFactory].
     */
    private val gradientRouteVM: GradientRouteVM by viewModels { GradientRouteVMFactory }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_route)

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(
                R.id.activity_gradient_route_map
            ) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->

            googleMap.uiSettings?.setAllGesturesEnabled(false)

            val builder = LatLngBounds.Builder()
            builder.include(LatLng(30.033333, 31.233334)) // Cairo LatLng Position.
            builder.include(LatLng(31.205753, 29.924526)) // Alexandria LatLng Position.
            val bounds = builder.build()

            /**
             * Zoom-in the [googleMap] to the Cairo-Alexandria boundaries with 20-degree padding
             * And then, observe on the Result<RouteUiModel> that contains the list of the route
             * coordinators.
             */
            googleMap.zoomCameraWithAnimation(CameraUpdateFactory.newLatLngBounds(bounds, 20)) {
                gradientRouteVM.getRouteList(context = this@GradientRouteActivity)
                    .observe(
                        this,
                        Observer { routeResult ->
                            showRoute(routeResult = routeResult, googleMap = googleMap)
                        }
                    )
            }

        }

    }

    /**
     * To cancel the coroutines job once the activity is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /**
     * This function is called once the [gradientRouteVM] returns with the [routeResult]
     *
     * @param routeResult is the Result<RouteUiModel> that contains list of the route coordinators.
     * @param googleMap is the instance of the main [googleMap].
     */
    private fun showRoute(
        routeResult: Result<RouteUiModel>,
        googleMap: GoogleMap
    ) {

        /**
         * Options for the polyline of the route between Cairo and Alexandria.
         */
        val routePolyLineOptions = PolylineOptions()
        routePolyLineOptions.width(10F)
        routePolyLineOptions.startCap(SquareCap())
        routePolyLineOptions.endCap(SquareCap())
        routePolyLineOptions.jointType(JointType.ROUND)

        /**
         * Initializing the [GradientPolyLine] with the custom polyline options, start
         * and end colors to make the gradient color calculation.
         */
        val gradientPolyLine = GradientPolyLine(map = googleMap, job = job)
            .setPolylineOptions(polylineOptions = routePolyLineOptions)
            .setDelayTime(delayTime = 10L)
            .setStartColor(startColor = ContextCompat.getColor(this, R.color.start_color))
            .setEndColor(endColor = ContextCompat.getColor(this, R.color.end_color))

        /**
         * Getting the route result and then draw the polyline.
         */
        routeResult.getOrNull()?.let { route ->
            gradientPolyLine.drawPolyline(route.coordinates) {
                googleMap.uiSettings?.setAllGesturesEnabled(true)
            }
        }

    }

}
