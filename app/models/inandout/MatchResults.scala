package models.inandout

case class MmeResponse(queryId: String, mmeResults: List[MmeResult])
case class MmeResult(id: String, label: String, features: List[ResponseFeature])
case class ResponseFeature(id: String, label: String)