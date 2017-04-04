package observatory.model

object Temperatures extends ArrayExtensions {
  def parse(line: String) = {
    val args = line.split(",")
    Temperatures(
      stn = args(0),
      wban = getIfDefinedAt(args, 1),
      month = getIfDefinedAt(args, 2).map(_.toInt),
      day = getIfDefinedAt(args, 3).map(_.toInt),
      temperature = getIfDefinedAt(args, 4).map(_.toDouble).map(toCelsius)
    )
  }

  def toCelsius(temp: Double) : Double = {
    BigDecimal((temp - 32) / 1.8).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }
}

case class Temperatures(stn: String, wban: Option[String], month: Option[Int], day: Option[Int], temperature: Option[Double])