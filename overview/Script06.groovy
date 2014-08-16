@Grab('com.github.albaker:GroovySparql:0.6')
import groovy.sparql.*


def builder = new RDFBuilder(new FileOutputStream('script6.out'))

builder.registerOutputHook { println "Finished building" }

def output = builder.turtle {

  namespace oslcrm:"http://open-services.net/ns/rm#"
  namespace rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  namespace dcterms:"http://purl.org/dc/terms/"

  for (i = 2; i < 10; i++) {

        subject("http://doors.example.com/reqs/$i") {
           predicate "rdf:type":new URI("http://open-services.net/ns/rm#Requirement")
           predicate "dcterms:title":"Requirement $i".toString()
           predicate("oslcrm:ImplementedBy") {
             subject("http://rtc.example.com/wi/$i") {
               predicate "rdf:type":new URI("http://open-services.net/ns/rm#ChangeRequest")
               predicate "dcterms:title":"Work Item $i".toString()
             }
           }
        }


  }
}


true
