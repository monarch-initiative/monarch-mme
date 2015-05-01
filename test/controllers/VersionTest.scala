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
  
  "checkAcceptedVersion" should {
    
  }
}