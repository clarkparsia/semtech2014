@Grab('com.github.albaker:GroovySparql:0.6')
import groovy.sparql.*

// SPARQL 1.0 or 1.1 endpoint
def sparql = new Sparql(endpoint:"http://localhost:5820/oslc/query", user:"admin", pass:"admin")

def prefixes = """
    PREFIX oslc: <http://open-services.net/ns/core#>
    PREFIX dcterms: <http://purl.org/dc/terms/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX oslc_qm: <http://open-services.net/ns/qm#>
    PREFIX oslc_cm: <http://open-services.net/ns/cm#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rep_qm: <http://jazz.net/xmlns/alm/qm/v0.1/>
    PREFIX hitss: <http://hitss.nasa.gov/>
"""

println "PLANS"
def query = """ ${prefixes}
    SELECT ?planUri {?planUri rdf:type oslc_qm:TestPlan}
"""

sparql.eachRow query, { row ->
    println row
}
