# stardog-groovy-examples

## Installation

1. Download (Stardog)[http://stardog.com]
2. Run `stardog-2.2.1/bin/mavenInstall`
3. Create the oslc database `bin/stardog-admin db create -n oslc --searchable -o strict.parsing=false -- /path/to/data/oslc/oslc_sample.ttl`
4. Browse to the (database)[http://localhost:5820/oslc]


## How to run

`gradle run -Pargs=NUM` where num is 11-17

Each sample script is in `src/main/groovy/org/example`.  The `App.groovy` connects to the Stardog database and passes that to each script which orchestrates a different aspect of the stardog-groovy API.

To import into Eclipse/STS use `gradle eclipse` and then `Import -> Existing Project` from the Eclipse menu.
