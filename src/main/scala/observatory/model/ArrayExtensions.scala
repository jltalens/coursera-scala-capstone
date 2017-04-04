package observatory.model

trait ArrayExtensions {

  def getIfDefinedAt(list: Array[String], idx: Int): Option[String] = {
    if (list.isDefinedAt(idx) && !list(idx).isEmpty)
      Some(list(idx))
    else
      None
  }

}
