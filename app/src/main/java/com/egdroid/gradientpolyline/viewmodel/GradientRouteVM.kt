package com.egdroid.gradientpolyline.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.egdroid.gradientpolyline.model.RouteUiModel
import com.egdroid.gradientpolyline.repo.DefaultRouteRepo
import com.egdroid.gradientpolyline.repo.RouteRepo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ahmed Adel on 5/3/20.
 *
 * Working as Senior Software Engineer at Zendesk
 * Email: ahmed.adel.said@gmail.com
 * Website: https://about.me/ahmed.adel.said/
 */
class GradientRouteVM(
    private val routeRepo: RouteRepo
) : ViewModel() {

    fun getRouteList(
        context: Context
    ): LiveData<Result<RouteUiModel>> = liveData {
        try {
            emit(Result.success(value = routeRepo.getRoute(context = context)))
        } catch (exception: Exception) {
            emit(Result.failure<RouteUiModel>(exception = exception))
        }
    }

}

/**
 * Factory for [GradientRouteVM].
 */
object GradientRouteVMFactory : ViewModelProvider.Factory {

    private val dataSource = DefaultRouteRepo(dispatcher = Dispatchers.IO)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GradientRouteVM(dataSource) as T
    }

}