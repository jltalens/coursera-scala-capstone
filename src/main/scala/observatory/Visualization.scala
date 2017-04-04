package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    ???
  }


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
  def estimate(p: Location, range: Iterable[(Location, Double)]) : Double = ???

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

}

