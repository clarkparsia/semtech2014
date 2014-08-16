@Grab('com.github.albaker:GroovySparql:0.6')
import groovy.sparql.*

def builder = new RDFBuilder(new FileOutputStream('script4.out'))
def output = builder.turtle {
        namespace oslcrm:"http://open-services.net/ns/rm#"
        namespace rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        namespace dcterms:"http://purl.org/dc/terms/"

        subject("http://doors.example.com/reqs/1") {
           predicate "rdf:type":new URI("http://open-services.net/ns/rm#Requirement")
           predicate "dcterms:title":"Requirement 1"
        }

}

true
