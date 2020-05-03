package com.egdroid.gradientpolyline.ext

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap

/**
 * Created by Ahmed Adel on 5/3/20.
 *
 * Working as Senior Software Engineer at Zendesk
 * Email: ahmed.adel.said@gmail.com
 * Website: https://about.me/ahmed.adel.said/
 */

/**
 * Extension function to zoom in the google maps according to the desired [CameraUpdate]
 * and fire a high ordered function [onFinishAnimation] after the zoom-in animation is finished.
 *
 * @param cameraUpdate is the desired location that the integrator wants the google maps
 * to zoom in.
 * @param onFinishAnimation is the delegate/callback that will be fired once the zoom in
 * animation is finished.
 */
inline fun GoogleMap.zoomCameraWithAnimation(
    cameraUpdate: CameraUpdate,
    crossinline onFinishAnimation: () -> Unit
) {
    setOnMapLoadedCallback {
        animateCamera(
            cameraUpdate,
            2000,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    onFinishAnimation.invoke()
                }

                override fun onCancel() {

                }
            }
        )
    }
}