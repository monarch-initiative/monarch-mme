package models.inandout

case class MatchQuery(responseType: String, features: List[Feature])
case class Feature(id: String, observed: String)