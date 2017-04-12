package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val width : Int = 360
  val height: Int = 180
  val dim : Int = width * height

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double =
    estimateTempAt(location, temperatures)



  /**
    *   Distance between two points using spherical law of cosines:
  	*          d = acos( sin φ1 ⋅ sin φ2 + cos φ1 ⋅ cos φ2 ⋅ cos Δλ ) ⋅ R
    *          being: φ -> latitude in radians; λ -> longitude in radians
    * @param p1 point 1
    * @param p2 point 1
    * @return distance between points
    */
  def distance(p1: Location, p2: Location): Double = {
    def toRadians(degrees: Double) = degrees * Math.PI / 180

    val R = 6371e3
    val φ1 = toRadians(p1.lat)
    val φ2 = toRadians(p2.lat)
    val λ1 = toRadians(p1.lon)
    val λ2 = toRadians(p2.lon)
    val Δλ = λ2 - λ1

    Math.acos(Math.sin(φ1) * Math.sin(φ2) + Math.cos(φ1) * Math.cos(φ2) * Math.cos(Δλ)) * R
  }

  /**
    * Uses the inverse distance weighting to estimate the temperate on a point given a range:
    *       u(x) = ui if (d(x, xi) == 0) for some i
    *              ∑(i=1,N) wi(x)ui / ∑(i=1,N) wi(x)
    *       being: wi(x) = 1 / d(x,xi) ** p
    *       being p a supplier power parameter
    * @param p
    * @param range
    * @return estimated temperature
    */
  def estimateTempAt(p: Location, range: Iterable[(Location, Double)]) : Double = range.find(_._1 == p) match {
    case Some((_, temp)) => temp
    case None => {
      val distanceTempSum = range
        .foldLeft(0.0)((acc, tuple) => acc + (invertedDistance(p, tuple._1) * tuple._2))
      val distanceSum = range
        .foldLeft(0.0)((acc, tuple) => acc + invertedDistance(p, tuple._1))
      distanceTempSum / distanceSum
    }
  }

  def invertedDistance(p1: Location, p2: Location) : Double = 1 / distance(p1, p2)


  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = points.find(_._1 == value) match {
    case Some((_, color)) => color
    case None => colorByLinearInterpolation(points, value)
  }

  //  Coger la linea de puntos. Coger los puntos que dejan al valor dado en medio:
  //
  def colorByLinearInterpolation(points: Iterable[(Double, Color)], value: Double): Color = {
    closestTwoPointsToGiven(points: Iterable[(Double, Color)], value: Double) match {
      case (Some((_, color)), None) => color
      case (None, Some((_, color))) => color
      case (Some((p1, c1)), Some((p2, c2))) => {
        val li = linearInterpolationValue(p1, p2, value) _
        Color(
          li(c1.red, c2.red),
          li(c1.green, c2.green),
          li(c1.blue, c2.blue)
        )
      }
      case _ => Color(0,0,0)
    }
  }

  def closestTwoPointsToGiven(points: Iterable[(Double, Color)], value: Double): (Option[(Double, Color)], Option[(Double, Color)]) = {
    val partition = points.toList.sortBy(_._1).partition(_._1 < value)
    (partition._1.reverse.headOption, partition._2.headOption)
  }


  def linearInterpolationValue(pointValueMin: Double, pointValueMax: Double, value: Double)(colorValueMin: Int, colorValueMax: Int): Int = {
    val factor = (value - pointValueMin) / (pointValueMax - pointValueMin)
    roundTo(colorValueMin + (colorValueMax - colorValueMin) * factor, 0).toInt
  }

  def roundTo(number: Double, decimals: Int) : Double =
    BigDecimal(number).setScale(decimals, BigDecimal.RoundingMode.HALF_UP).toDouble

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val imageWidth = Visualization.width
    val imageHeight = Visualization.height
    val locationMap = posToLocation(imageWidth, imageHeight) _

    val pixels = (0 until imageHeight * imageWidth).par.map {
      pos =>
        pos -> interpolateColor(
          colors,
          predictTemperature(
            temperatures,
            locationMap(pos)
          )
        ).pixel()
    }
      .seq
      .sortBy(_._1)
      .map(_._2)

    Image(imageWidth, imageHeight, pixels.toArray)
  }
//    temperatures
//      .map{p =>
//        val color = interpolateColor(colors, p._2)
//        (p._1, Pixel(color.red, color.green, color.blue, 1))
//      }
//  }
//
//  def locationsToPixelArray(temps: Iterable[(Location, Pixel)]): Array[Pixel] = {
//    ???
////    var pixels : Array[Pixel] = Array.ofDim(Visualization.dim)
////    temps.foreach{ p =>
////
////    }
//  }
//
//  def coordinatesToArrayIdx(lon: Double, lat: Double): Int = {
//    roundTo(lat, 0).toInt + 180
//  }

  def posToLocation(imageWidth: Int, imageHeight: Int)(pos: Int): Location = {
    val widthFactor = 180 * 2 / imageWidth.toDouble
    val heightFactor = 90 * 2 / imageHeight.toDouble

    val x: Int = pos % imageWidth
    val y: Int = pos / imageWidth

    Location(90 - (y * heightFactor), (x * widthFactor) - 180)
  }

}

