package org.example

class Script16 {

	def exec(db) {
		
		// Groovy closure, note the "with" method applies method invocation on the parent obj
		// So "nextInt" is from the Random object, iterated upon within the with closure
		// collect = map function.  (x..y) is a range, produces a list of the values in the range
		def generator = { String alphabet, int n ->
			new Random().with {
			  (1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
			}
		  }
		
		def identifier = "urn:identifier:1" 
		def predicate = "urn:test:1"
		def dataSet = []
		
		(0..5).each { 
			// Create some random data
			def key = generator( (('A'..'Z')+('0'..'9')).join(), 4 )
			dataSet << [identifier + it, predicate, key]
		}
		
		println dataSet
		
		db.insert(dataSet)
		
		db.query "select ?s ?o where { ?s <urn:test:1> ?o }", {
			println it
		}
		
	}
	
}
