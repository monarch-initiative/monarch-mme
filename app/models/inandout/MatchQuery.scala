package models.inandout

case class MatchQuery(patient: Patient)
case class Patient(id: String, label: Option[String], contact: Contact, species: Option[String],
                   sex: Option[String], ageOfOnset: Option[String], inheritanceMode: Option[String],
                   disorders: Option[List[Disorder]], features: Option[List[Feature]], genomicFeatures: Option[List[GenomicFeature]])
case class Contact(name: String, institution: Option[String], href: String)
case class Disorder(id: String)
case class Feature(id: String, observed: String, label: String)
case class GenomicFeature(gene: Gene, variant: Option[Variant], zygosity: Option[Int], typeInfo: TypeInfo)
case class Gene(id: String)
case class Variant(assembly: String, referenceName: String, start: Int, end: Option[Int],
                   referenceBases: Option[String], alternateBases: Option[String])
case class TypeInfo(id: String, label: Option[String])