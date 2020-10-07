plugins {
   id("us.ihmc.ihmc-build") version "0.22.0"
   id("us.ihmc.ihmc-ci") version "6.4"
   id("us.ihmc.ihmc-cd") version "1.7"
}

ihmc {
   group = "us.ihmc"
   version = "0.12.5"
   vcsUrl = "https://github.com/ihmcrobotics/ihmc-geometry"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("us.ihmc:euclid-geometry:0.12.1")
   api("org.ejml:core:0.30")
   api("org.ejml:dense64:0.30")
   api("us.ihmc:ihmc-commons:0.26.6")
   api("net.sf.trove4j:trove4j:3.0.3")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.26.6")
}
