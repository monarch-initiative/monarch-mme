package controllers

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.ScalaCheck

@RunWith(classOf[JUnitRunner])
class VersionTest extends Specification with ScalaCheck {
  "apiVersionComptabilityCheck" should {
    "detect incompatible MAJOR versions" in prop {
      (rndUserMajor: Int, rndUserMinor: Int, rndServerMajor: Int, rndServerMinor: Int) =>
        (rndUserMajor != rndServerMajor) ==> {
          Version.apiVersionComptabilityCheck(rndUserMajor, rndUserMinor, rndServerMajor, rndServerMinor) must beFalse
        }
    }

    "detect incompatible MINOR versions" in prop {
      (rndrMajor: Int, rndUserMinor: Int, rndServerMinor: Int) =>
        (rndUserMinor > rndServerMinor) ==> {
          Version.apiVersionComptabilityCheck(rndrMajor, rndUserMinor, rndrMajor, rndServerMinor) must beFalse
        }
    }

    "pass with compatible versions" in prop {
      (rndrMajor: Int, rndUserMinor: Int, rndServerMinor: Int) =>
        (rndServerMinor > rndUserMinor) ==> {
          Version.apiVersionComptabilityCheck(rndrMajor, rndUserMinor, rndrMajor, rndServerMinor) must beTrue
        }
    }

  }

  "extractMajorMinorVersion" should {
    "successfully extract version" in {
      val versionOpt = Version.extractMajorMinorVersion("application/vnd.ga4gh.matchmaker.v0.7+json")
      versionOpt.isDefined === true
      versionOpt.get._1 === 0
      versionOpt.get._2 === 7
    }
    "not find any version" in {
      val versionOpt = Version.extractMajorMinorVersion("hellokitty")
      versionOpt.isDefined == false
    }
  }

  "checkAcceptedVersion" should {
    "not find version in empty header" in {
      Version.checkAcceptedVersion(List.empty) must beFalse
    }

    "find version in provided header" in {
      Version.checkAcceptedVersion(List(Version.version)) must beTrue
    }

    "find version with many headers" in {
      Version.checkAcceptedVersion(List(Version.version, "hellokitty", "batman")) must beTrue
    }
  }
}