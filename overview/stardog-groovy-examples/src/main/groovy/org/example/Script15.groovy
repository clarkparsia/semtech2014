package org.example

class Script15 {

	def exec(db) {
		
		db.insert([
			"http://rtc.example.com/wi/3", 
			"http://www.w3.org/1999/02/22-rdf-syntax-ns#", 
			new URI("http://open-services.net/ns/rm#ChangeRequest"
		)])
		
		db.query "select ?p ?o where { <http://rtc.example.com/wi/3> ?p ?o }", {
			println it
		}
		
	}
	
}
