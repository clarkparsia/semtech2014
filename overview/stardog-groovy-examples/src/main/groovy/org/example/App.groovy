package org.example

import com.complexible.stardog.ext.groovy.*
import static com.xlson.groovycsv.CsvParser.parseCsv

public class App {

   static main(args) {

      println args
      def db = new Stardog([url: "snarl://localhost:5820/", to:"oslc", username:"admin", password:"admin"])

	  def scripts = ["11":new Script11(),
		  			 "12":new Script12(),
					 "13":new Script13(),
					 "14":new Script14(),
					 "15":new Script15(),
					 "16":new Script16(),
					 "17":new Script17()]
	  
	  
	  def target
	  if (!args) { 
		  target = "11"
	  } else { 
	  	  target = args[0] as String
	  }
	  
	  println "Running script: $target"
	  scripts[target].exec(db)
	  

	  /*
      def f = new FileReader('/Users/albaker/Reqs.csv')
      def data = parseCsv(f)
      for (line in data) {
        println line
      }
      */

   }

}


