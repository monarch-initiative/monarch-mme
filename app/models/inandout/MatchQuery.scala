package models.inandout

case class MatchQuery(patient: Patient)
case class Patient(id: String, label: Option[String] = None, contact: Contact, species: Option[String] = None,
                   sex: Option[String] = None, ageOfOnset: Option[String] = None, inheritanceMode: Option[String] = None,
                   disorders: Option[List[Disorder]] = None, features: Option[List[Feature]] = None, genomicFeatures: Option[List[GenomicFeature]] = None)
case class Contact(name: String, institution: Option[String], href: String)
case class Disorder(id: String)
case class Feature(id: String, observed: Option[String], ageOfOnset: Option[String])
case class GenomicFeature(gene: Gene, variant: Option[Variant], zygosity: Option[Int], typeInfo: TypeInfo)
case class Gene(id: String)
case class Variant(assembly: String, referenceName: String, start: Int, end: Option[Int],
                   referenceBases: Option[String], alternateBases: Option[String])
case class TypeInfo(id: String, label: Option[String])