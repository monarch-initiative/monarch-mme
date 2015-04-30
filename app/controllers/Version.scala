package controllers

object Version {
  def version = "application/vnd.ga4gh.matchmaker.v0.7+json" //Content-Type: application/vnd.ga4gh.matchmaker.v0.7+json

  def checkAcceptedVersion(accepts: List[String]): Boolean = {
    val userVersion = accepts.map(extractMajorMinorVersion).filter(_.isDefined)
    if (userVersion.isEmpty || userVersion.size > 1) {
      false
    } else {
      val (major, minor) = userVersion.apply(0).get
      val (serverMajor, serverMinor) = extractMajorMinorVersion(version).get
      ApiVersionComptabilityCheck(major, minor, serverMajor, serverMinor)
    }
  }

  /* Input like application/vnd.ga4gh.matchmaker.v0.7+json */
  def extractMajorMinorVersion(version: String): Option[(Int, Int)] = {
    val regex = """application/vnd.ga4gh.matchmaker.v(d+)\.(d+)+json""".r
    version match {
      case regex(major, minor) => Some((major.toInt, minor.toInt))
      case _                   => None
    }
  }

  def ApiVersionComptabilityCheck(userMajor: Int, userMinor: Int, serverMajor: Int, serverMinor: Int): Boolean = {
    userMajor == serverMajor && userMinor <= serverMinor
  }
}