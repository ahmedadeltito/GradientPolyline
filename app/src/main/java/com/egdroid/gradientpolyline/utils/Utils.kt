package com.egdroid.gradientpolyline.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Created by Ahmed Adel on 5/3/20.
 *
 * Working as Senior Software Engineer at Zendesk
 * Email: ahmed.adel.said@gmail.com
 * Website: https://about.me/ahmed.adel.said/
 */
suspend fun getJsonDataFromAsset(
    context: Context,
    fileName: String
): String? = withContext(Dispatchers.IO) {
    var jsonString: String? = null
    try {
        jsonString = context
            .assets
            .open(fileName)
            .bufferedReader()
            .use {
                it.readText()
            }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
    }
    jsonString
}