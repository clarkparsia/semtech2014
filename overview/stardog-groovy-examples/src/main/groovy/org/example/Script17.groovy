package org.example

class Script17 {

	def exec(db) {
		
		// Get the list of IDs generated in the last example
		def testIds = []
		
		db.each "select ?s ?o where { ?s <urn:test:1> ?o }", {
			testIds << [s as String]
		}
		
		println testIds
		
		if (testIds.size() == 0) { return } 
		
		// Now let's get them out of our nice OSLC database...
		
		testIds.each { id ->
			// remove takes a list, up to 3 elements for subject, predicate, object to match multiple triples..
			db.remove id
		}
		
		db.query "select ?s ?o where { ?s <urn:test:1> ?o }", {
			println it
		}
		
		println "Done"
	}
	
}
