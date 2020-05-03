
# Gradient Polyline  

> A use case for creating a gradient polyline on Google Maps in Android natively. Since it is not a built-in API in Google Maps Android SDK.

<img src="https://github.com/ahmedadeltito/GradientPolyline/blob/master/art/gradient_polyline.gif"/>

## Case Study  

If you want to create a gradient polyline between two locations on Andoird Google Maps, you won't be able since it is not a built-in API in Android Google Maps SDK according to this [stackoverflow](https://stackoverflow.com/a/43758358/6264095) comment. That makes me try if I could make it natively or it would be something hard to implement. And I found that - in 6 hours - it could be something double and you just need to think about it in an out-of-the-box mindset and give it a try.

## How `GradientPolyLine` class is created ? 

Since we are going to implement the function that creates the gradient color by ourselves, so we should know how the gradient color is created programmatically.

But before digging deeper into this, we want to mention that, to be able to draw a gradient polyline, we should have first the route - list of coordinates - that we will use to draw the gradient polyline for.

In the `assets` folder, you will find a `route.json` file that contains the list of coordinates which indicates the route between Cairo, Egypt to Alexandria, Egypt.

Let's go back to the gradient thing, So, for any gradient color, it creates from two colors `startColor` and `endColor`. So first thing, we should extract the RGB for these two colors

    /**
    * Extract the RGD colors from the startColor
    */
    val startRed = Color.red(startColor)
    val startGreen = Color.green(startColor)
    val startBlue = Color.blue(startColor)

    /**
    * Extract the RGD colors from the endColor
    */
    val endRed = Color.red(endColor)
    val endGreen = Color.green(endColor)
    val endBlue = Color.blue(endColor)

After this, we should calculate the steps for each RGB color by subtracting the `endColor` from the `startColor` divided by 255 - which is alpha - and divided by coordinates list size of the Cairo, Alexandria route.

    /**
    * Calculate the steps for each RGB color.
    */
    val redSteps = (endRed - startRed).toFloat() / 255 / simplifiedListSize
    val greenSteps = (endGreen - startGreen).toFloat() / 255 / simplifiedListSize
    val blueSteps = (endBlue - startBlue).toFloat() / 255 / simplifiedListSize

Then, we can now generate the RGB colors for gradient by getting each start RGB color and dividing them by 255 - which is alpha -, then getting the result from this plus it to the multiplication of the RGB step colors with the for loop index of the route coordinates list.

    /**
    * Generate the RGB colors for gradient by getting each start RGB color and
    * dividing them by 255. Then getting the result and plus it to
    * the multiplication of the RGB step colors with the for loop index of
    * the simplifiedList.
    */
    val redGradientColor = (startRed.toFloat() / 255) + (redSteps * index)
    val greenGradientColor = (startGreen.toFloat() / 255) + (greenSteps * index)
    val blueGradientColor = (startBlue.toFloat() / 255) + (blueSteps * index)
    
    /**
    * Then generate the full color.
    */
    val gradientColor = getRGBColor(
        red = redGradientColor,
        green = greenGradientColor,
        blue = blueGradientColor
    )

Now, the gradient color calculation is done for each route coordinate. We need to add this in a meaningful way that the `PolylineOptions` of the Google Maps can understand and then we can draw the gradient polyline on the Google Maps. So, we create a `val gradientPoly: ArrayList<PolylineOptions> = ArrayList()` and for each coordinate iteration, a `polylineOptions` with its `gradientColor` will be added in the `gradientPoly` array.

    /**
    * And add it to the gradientPoly array of PolylineOptions
    */
    gradientPoly.add(
        copyPolylineOptions(polylineOptions)
            .color(gradientColor)
            .add(simplifiedList[index])
            .add(simplifiedList[index + 1])
    )

Finally, we can now add all the `PolylineOptions` we have in the `gradientPoly` array in Google Maps by iterating over them, and for each iteration, we will call `map.addPolyline(polylineOptions)` to add each `polylineOption` with its specific gradient color.

    /**
    * Then add each polylineOptions that are stored in the gradientPoly on the
    * map with the delay that the integrator set.
    */
    withContext(Dispatchers.Main) {
        setZoomingOnMap(false)
        gradientPoly.forEach { polylineOptions ->
            map.addPolyline(polylineOptions)
            delay(delayTime)
        }
        setZoomingOnMap(true)
        onDrawFinished()
    }

## Application architecture and structure: 

It is a single module project architecture. And, I'm following the **Clean Architecture** as a Software Architecture Pattern and  **MVVM** as a Presentation Layer Pattern.

The architecture is inspired by the [LiveDataSample](https://github.com/android/architecture-components-samples/tree/master/LiveDataSample) from the android component simples.

## Third-party used: 

I was trying to use the things that make the development process much easier and faster to be focused more on the desired from the use case which is creating a gradient polyline.

- [Coroutines](https://developer.android.com/kotlin/coroutines) for handling the background operations like reading the JSON from a file, parsing it, and looping all over the coordinates to fill the `gradientPoly` with `PolylineOptions` with its gradient color.
- [Moshi](https://github.com/square/moshi/) for parsing the JSON from `route.json` file and start working on the Cairo/Alexandria route.
- [Android Maps Utils](https://github.com/googlemaps/android-maps-utils) for Simplifies the given coordinate list using the Douglas-Peucker decimation algorithm.
- [KTX dependencies](https://developer.android.com/topic/libraries/architecture/coroutines) for making the implementation between Coroutines and MVVM much easier.


## Contributing
Any contributions are more than welcomed from other developers to help us make the SDK even better.
Before you contribute there are a number of things that you should know please see [CONTRIBUTING.md](https://github.com/ahmedadeltito/GradientPolyline/blob/master/CONTRIBUTING.md) for details.


## License
[MIT License](https://github.com/ahmedadeltito/GradientPolyline/blob/master/LICENSE)

Copyright (c) 2020 Ahmed Adel
