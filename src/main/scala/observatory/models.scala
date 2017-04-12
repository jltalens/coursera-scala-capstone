package observatory

import com.sksamuel.scrimage.RGBColor
import scala.math._

case class Location(lat: Double, lon: Double)

case class Color(red: Int, green: Int, blue: Int) {
  def pixel(alpha: Int = 255) = RGBColor(red, green, blue, alpha).toPixel
}

case class Tile(x: Double, y: Double, z: Int) {
  def toLocation = Location(
    toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y / (1 << z))))),
    x / (1 << z) * 360.0 - 180.0
  )
  def toURI = new java.net.URI("http://tile.openstreetmap.org/"+z+"/"+x+"/"+y+".png")
}