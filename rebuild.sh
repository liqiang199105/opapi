mvn eclipse:clean
mvn eclipse:eclipse -Declipse.useProjectReferences=false -Dwtpversion=2.0 -U 
mvn dependency:sources
