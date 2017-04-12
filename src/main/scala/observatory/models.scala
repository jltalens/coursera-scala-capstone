package observatory

import com.sksamuel.scrimage.RGBColor

case class Location(lat: Double, lon: Double)

case class Color(red: Int, green: Int, blue: Int) {
  def pixel(alpha: Int = 255) = RGBColor(red, green, blue, alpha).toPixel
}

