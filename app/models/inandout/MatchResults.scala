package models.inandout

case class MmeResponse(results: List[Result])
case class Result(score: PatientScore, patient: Patient)
case class PatientScore(patient: Double)
