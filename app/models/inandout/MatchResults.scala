package models.inandout

case class MmeResponse(queryId: String, mmeResults: Seq[MmeResult])
case class MmeResult(id: String, label: String)