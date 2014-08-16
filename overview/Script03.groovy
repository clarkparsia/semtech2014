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

println "TESTCASES"
query = """ ${prefixes}
    SELECT ?caseUri ?caseName {
        ?planUri rdf:type oslc_qm:TestPlan .
        ?planUri dcterms:title "Geb Automated Tests"^^xsd:string .
        ?planUri oslc_qm:usesTestCase ?caseUri .
        ?caseUri dcterms:title ?caseName
    }
"""

def cases = [:]
sparql.each query, {
    println "${caseUri} : ${caseName}"
    cases.put("${caseUri}", "${caseName}")
}

cases.each {k, v ->
    println v
    query = """ ${prefixes}
        SELECT ?resultUri ?resName ?browser ?resultStatus ?terUri ?html ?png  {
            ?resultUri a oslc_qm:TestResult .
            ?resultUri oslc_qm:reportsOnTestCase <${k}> .
            ?resultUri oslc_qm:status ?resultStatus .
            ?resultUri dcterms:title ?resName .
            ?resultUri oslc_qm:producedByTestExecutionRecord ?terUri .
            ?terUri oslc_qm:runsOnTestEnvironment ?env .
            ?env rep_qm:name ?browser .
            OPTIONAL {?resultUri hitss:producesHtmlSnapshot ?html} .
            OPTIONAL {?resultUri hitss:producesPngSnapshot ?png} .

        }
        ORDER BY ?resName
        """

    println query

    sparql.each query, {
        println "${terUri} : ${browser}\n\t${resultUri} : ${resultStatus}"
    }
}


true
