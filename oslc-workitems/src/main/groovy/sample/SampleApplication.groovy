/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam;

import static com.xlson.groovycsv.CsvParser.parseCsv

import javax.annotation.PostConstruct;
import groovy.sparql.*

@Configuration
@EnableAutoConfiguration
@RestController
class SampleApplication {

   def prefixes = """
        PREFIX oslc: <http://open-services.net/ns/core#>
        PREFIX dcterms: <http://purl.org/dc/terms/>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX oslc_qm: <http://open-services.net/ns/qm#>
        PREFIX oslc_cm: <http://open-services.net/ns/cm#>
        PREFIX oslc_rm: <http://open-services.net/ns/rm#>
        PREFIX qm: <http://open-services.net/ns/qm#>
        PREFIX cm: <http://open-services.net/ns/cm#>
        PREFIX rm: <http://open-services.net/ns/rm#>
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        PREFIX rep_qm: <http://jazz.net/xmlns/alm/qm/v0.1/>
        PREFIX hitss: <http://hitss.nasa.gov/>
    """

  def model

  @PostConstruct
  def init() {


      def f = new FileReader('/Users/albaker/Reqs.csv')
      def data = parseCsv(f)
      def builder = new RDFBuilder()

      def output = builder.turtle {
            namespace oslcrm:"http://open-services.net/ns/rm#"
            namespace rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
            namespace dcterms:"http://purl.org/dc/terms/"
            namespace foaf:"http://xmlns.com/foaf/0.1/"

            for (line in data) {
              def poc = new URI()
              subject("http://doors.example.com/reqs/" + line.reqId) {
                 predicate "rdf:type":new URI("http://open-services.net/ns/rm#ChangeRequest")
                 predicate "dcterms:title":line.reqTitle
                 predicate "dcterms:description":line.reqDescription
                 predicate "rdf:type":new URI("http://doors.example.com/class/" +line.reqType)
                 predicate ("dcterms:creator") {
                   subject("http://doors.example.com/person/" + line.reqCreator) {
                     predicate "foaf:firstName":line.reqCreator
                   }
                 }

              }

            }
      }
      model = output

    }



	@RequestMapping("/")
	def helloWorld(OutputStream stream) {

    	  /*
      def f = new FileReader('/Users/albaker/Reqs.csv')
      def data = parseCsv(f)
      for (line in data) {
        println line
      }
      */

    def sparql = new Sparql(model)



    def query = """${prefixes}
        CONSTRUCT {
            ?reqUri rdf:type oslc_rm:ChangeRequest .
            ?reqUri dcterms:title ?title .
            ?reqUri dcterms:description ?description .
        } WHERE {
            ?reqUri rdf:type oslc_rm:ChangeRequest .
            ?reqUri dcterms:title ?title .
            ?reqUri dcterms:description ?description .

        }

    """

    def constructModel = sparql.construct(query)


    def builder = new RDFBuilder()
    def output = builder.turtle {
            namespace oslcrm:"http://open-services.net/ns/rm#"
            namespace rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
            namespace dcterms:"http://purl.org/dc/terms/"
            subject("http://doors.example.com/reqs/1") {
               predicate "rdf:type":"http://open-services.net/ns/rm#ChangeRequest"
               predicate "oslcrm:ImplementedBy": new URI("http://rtc.example.com/wi/2")
            }

    }

    //output.write(stream, "TTL")

    constructModel.write(stream, "TTL")
    null
	}

  @RequestMapping("/database")
  def database(OutputStream stream) {

    model.write(stream, "RDF/XML")
    null
  }


  // Very simple example of an OSLC query service built with SPARQL construct
  @RequestMapping("/req")
  def reqservice(@RequestParam(value="oslc.where", required=false, defaultValue="World") String query, OutputStream stream) {

    def predicates = []

    if (query.contains(" and ")) {
      query.split(" and ").each {
        predicates << it
      }
    } else {
      predicates << query
    }

    def predicateFragment = ""
    predicates.each {
      def tokens = it.split("=")

      predicateFragment += "?req ${tokens[0]} ${tokens[1]} . "
    }

    def sparqlQuery = """${prefixes}
    CONSTRUCT {
      ?req a oslc_rm:ChangeRequest .
      $predicateFragment

    } where {
      ?req a oslc_rm:ChangeRequest .
      $predicateFragment
    }

"""
    def sparql = new Sparql(model)
    def output = sparql.construct(sparqlQuery)
    output.write(stream, "TTL")
    null

  }

	static void main(String[] args) throws Exception {
		SpringApplication.run(SampleApplication.class, args)
	}

}
