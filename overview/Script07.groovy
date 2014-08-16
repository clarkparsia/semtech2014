@Grab('com.complexible.stardog:stardog-groovy:2.2.1')
import com.complexible.stardog.ext.groovy.*


def db = new Stardog([url: "snarl://localhost:5820/", to:"oslc", username:"admin", password:"admin"])


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
def sparql = """ ${prefixes}
    SELECT ?planUri {?planUri rdf:type oslc_qm:TestPlan}
"""

db.query sparql, {
    println it
}

true
