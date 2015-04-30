//package controllers
//
//import org.specs2.mutable.Specification
//import org.junit.runner.RunWith
//import org.specs2.runner.JUnitRunner
//
//@RunWith(classOf[JUnitRunner])
//class VersionTest extends Specification with ScalaCheck {
//  "ApiVersionComptabilityCheck" should {
//    "detect incompatible MAJOR versions" in prop {
//      (rndUserMajor: Int, rndUserMinor: Int, rndServerMajor: Int, rndServerMinor: Int) =>
//        (rndUserMajor != rndServerMajor) ==> {
//          Version.ApiVersionComptabilityCheck(rndUserMajor, rndUserMinor, rndServerMajor, rndServerMinor) must beFalse
//        }
//    }
//
//    "detect incompatible MINOR versions" in prop {
//      (rndUserMajor: Int, rndUserMinor: Int, rndServerMajor: Int, rndServerMinor: Int) =>
//        (rndUserMajor == rndServerMajor && rndUserMinor > rndServerMinor) ==> {
//          Version.ApiVersionComptabilityCheck(rndUserMajor, rndUserMinor, rndServerMajor, rndServerMinor) must beFalse
//        }
//    }
//  }
//}