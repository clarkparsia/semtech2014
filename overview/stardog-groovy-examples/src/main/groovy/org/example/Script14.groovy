package org.example

import org.openrdf.query.TupleQueryResult
import org.openrdf.model.impl.CalendarLiteralImpl;
import com.complexible.stardog.api.SelectQuery


class Script14 {

	def exec(db) {
		
		// Wraps in a transaction
		db.withConnection { con ->
			def queryString = """
				SELECT ?s ?p ?o
				{ 
				  ?s ?p ?o
				}
			"""
			TupleQueryResult result = null;
			try {
				
				SelectQuery query = con.select(queryString);
				result = query.execute();
				while (result.hasNext()) {
					println result.next();
				}

				result.close(); // still must close your result sets!


			} catch (Exception e) {
				println "Caught exception ${e}"
			}
		}
		
		
	}
	
}
