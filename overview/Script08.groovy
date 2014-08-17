@Grab('com.github.albaker:GroovySparql:0.7.2')
import groovy.sparql.*


// Step 1: Use the builder DSL to create requirements
def builder = new RDFBuilder()

def model = builder.turtle {

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
           if (i.toBigInteger().mod(2)==0) {
             predicate "dcterms:subject":"Review Complete"
           }
        }


  }
}

// Step 2: Query the model using the Sparql class
def sparql = new Sparql(model)

def query = """
    SELECT ?subject ?object {?subject ?property ?object }
"""

sparql.eachRow( query, [ property: new URI("http://purl.org/dc/terms/subject")], { row ->
			println row
})

true
