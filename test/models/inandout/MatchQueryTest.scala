package models.inandout

import org.specs2.mutable.Specification
import scala.io.Source
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.libs.json._
import play.api.libs.functional.syntax._
import controllers._

@RunWith(classOf[JUnitRunner])
class MatchQueryTest extends Specification {

  "MatchQuery" should {
    "successfully parse test json" in {
      val json: JsValue = Json.parse(Source.fromURL(getClass.getResource("/test.json")).mkString)
      checkJsResult(json.validate[Seq[Patient]]) // TODO should be MatchQuery
    }

    "successfully parse Contact" in {
      val input: String = """
        {
          "href": "http://www.ncbi.nlm.nih.gov/pubmed/22305528",
          "institution": "Children's Hospital of Eastern Ontario",
          "name": "Lijia Huang"
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Contact])
    }

    "successfully parse TypeInfo" in {
      val input: String = """
        {
          "id": "SO:0001587",
          "label": "STOPGAIN"
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[TypeInfo])
    }

    "successfully parse Variant" in {
      val input: String = """
        {
          "assembly": "GRCh37",
          "hgvs": "NM_004247.3(EFTUD2):c.2770C>T",
          "referenceName": "17",
          "start": 42929131
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Variant])
    }

    "successfully parse Gene" in {
      val input: String = """
        {
          "id": "EFTUD2"
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Gene])
    }

    "successfully parse GenomicFeature" in {
      val input: String = """
        {
          "gene": {
            "id": "EFTUD2"
          },
          "type": {
            "id": "SO:0001587",
            "label": "STOPGAIN"
          },
          "variant": {
            "assembly": "GRCh37",
            "hgvs": "NM_004247.3(EFTUD2):c.2770C>T",
            "referenceName": "17",
            "start": 42929131
          }
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[GenomicFeature])
    }
    
    "successfully parse Feature" in {
      val input: String = """
        {
          "id": "HP:0008773",
          "label": "Aplasia/Hypoplasia of the middle ear",
          "observed": "yes"
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Feature])
    }
    
    "successfully parse Disorder" in {
      val input: String = """
        {
          "id": "MIM:610536",
          "label": "#610536 MANDIBULOFACIAL DYSOSTOSIS, GUION-ALMEIDA TYPE; MFDGA ;;MANDIBULOFACIAL DYSOSTOSIS WITH MICROCEPHALY; MFDM;; GROWTH AND MENTAL RETARDATION, MANDIBULOFACIAL DYSOSTOSIS, MICROCEPHALY, AND CLEFT PALATE"
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Disorder])
    }
    
    "successfully parse Patient" in {
      val input: String = """
        {
          "contact": {
            "href": "http://www.ncbi.nlm.nih.gov/pubmed/22305528",
            "institution": "Children's Hospital of Eastern Ontario",
            "name": "Lijia Huang"
          },
          "disorders": [
            {
              "id": "MIM:610536",
              "label": "#610536 MANDIBULOFACIAL DYSOSTOSIS, GUION-ALMEIDA TYPE; MFDGA ;;MANDIBULOFACIAL DYSOSTOSIS WITH MICROCEPHALY; MFDM;; GROWTH AND MENTAL RETARDATION, MANDIBULOFACIAL DYSOSTOSIS, MICROCEPHALY, AND CLEFT PALATE"
            }
          ],
          "features": [
            {
              "id": "HP:0008773",
              "label": "Aplasia/Hypoplasia of the middle ear",
              "observed": "yes"
            }
          ],
          "genomicFeatures": [
            {
              "gene": {
                "id": "EFTUD2"
              },
              "type": {
                "id": "SO:0001587",
                "label": "STOPGAIN"
              },
              "variant": {
                "assembly": "GRCh37",
                "hgvs": "NM_004247.3(EFTUD2):c.2770C>T",
                "referenceName": "17",
                "start": 42929131
              }
            }
          ],
          "id": "P0000079",
          "label": "178_M43377",
          "test": true
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Patient])
    }
    
    "successfully parse MatchQuery" in {
      val input: String = """
        {
        "contact": {
          "href": "http://www.ncbi.nlm.nih.gov/pubmed/22305528",
          "institution": "Children's Hospital of Eastern Ontario",
          "name": "Lijia Huang"
        },
        "disorders": [
          {
            "id": "MIM:610536",
            "label": "#610536 MANDIBULOFACIAL DYSOSTOSIS, GUION-ALMEIDA TYPE; MFDGA ;;MANDIBULOFACIAL DYSOSTOSIS WITH MICROCEPHALY; MFDM;; GROWTH AND MENTAL RETARDATION, MANDIBULOFACIAL DYSOSTOSIS, MICROCEPHALY, AND CLEFT PALATE"
          }
        ],
        "features": [
          {
            "id": "HP:0008773",
            "label": "Aplasia/Hypoplasia of the middle ear",
            "observed": "yes"
          },
          {
            "id": "HP:0000413",
            "label": "Atresia of the external auditory canal",
            "observed": "yes"
          },
          {
            "id": "HP:0000453",
            "label": "Choanal atresia",
            "observed": "yes"
          },
          {
            "id": "HP:0000405",
            "label": "Conductive hearing impairment",
            "observed": "yes"
          },
          {
            "id": "HP:0011451",
            "label": "Congenital microcephaly",
            "observed": "yes"
          },
          {
            "id": "HP:0011471",
            "label": "Gastrostomy tube feeding in infancy",
            "observed": "yes"
          },
          {
            "id": "HP:0010880",
            "label": "Increased nuchal translucency",
            "observed": "yes"
          },
          {
            "id": "HP:0000272",
            "label": "Malar flattening",
            "observed": "yes"
          },
          {
            "id": "HP:0000347",
            "label": "Micrognathia",
            "observed": "yes"
          },
          {
            "id": "HP:0008551",
            "label": "Microtia",
            "observed": "yes"
          },
          {
            "id": "HP:0011342",
            "label": "Mild global developmental delay",
            "observed": "yes"
          },
          {
            "id": "HP:0000545",
            "label": "Myopia",
            "observed": "yes"
          },
          {
            "id": "HP:0000384",
            "label": "Preauricular skin tag",
            "observed": "yes"
          },
          {
            "id": "HP:0001622",
            "label": "Premature birth",
            "observed": "no"
          },
          {
            "id": "HP:0009623",
            "label": "Proximal placement of thumb",
            "observed": "yes"
          }
        ],
        "genomicFeatures": [
          {
            "gene": {
              "id": "EFTUD2"
            },
            "type": {
              "id": "SO:0001587",
              "label": "STOPGAIN"
            },
            "variant": {
              "assembly": "GRCh37",
              "hgvs": "NM_004247.3(EFTUD2):c.2770C>T",
              "referenceName": "17",
              "start": 42929131
            }
          }
        ],
        "id": "P0000079",
        "label": "178_M43377",
        "test": true
        }
        """
      val json: JsValue = Json.parse(input)
      checkJsResult(json.validate[Patient]) // TODO should be MatchQuery
    }

    def checkJsResult[T](result: JsResult[T]): Boolean = {
      result.fold(
        errors => {
          println(JsError.toFlatJson(errors))
          false
        },
        matchQueryObj => {
          true
        })
    }
  }

}