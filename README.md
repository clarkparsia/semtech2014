# Semtech 2014 Languages Tutorial

This repo provides the data, code, and sample projects shown at the 2014 SemTech Biz conference in San Jose.

## Installation

1. Download (Stardog)[http://stardog.com]
2. Run `stardog-2.2.1/bin/mavenInstall`
3. Create the oslc database `bin/stardog-admin db create -n oslc --searchable -o strict.parsing=false -- /path/to/data/oslc/oslc_sample.ttl`
4. Browse to the (database)[http://localhost:5820/oslc]


For the Groovy scripts,
1. Download GVM from gvmtool.net
2. Install groovy 2.2.2
3. Then run `groovy Script.groovy` in the overview directory


For Stardog-groovy
1. Repeat the Groovy scripts
2. Install Gradle 1.9 with GVM
3. Run `gradle run -Pargs=num`, where num is the number of the sample script


For Clojure
1. Download leiningen 2.x via http://leiningen.org/
2. Install the Leiningen oneoff plugin, see here: https://github.com/mtyaka/lein-oneoff
3. Run the Script.clj files with `lein oneoff Script.clj`

For oslc-workitems, the Spring Boot REST Linked Data app:
1. Repeat the Groovy scripts install
2. Install Gradle
3. Run `gradle run`

For oslc-quality, the Stardog-clj CRUD application
1. Download leiningen 2.x
2. Run `lein ring server`

## Questions

Any questions, please refer to the (Stardog Google Group)[https://groups.google.com/a/clarkparsia.com/forum/#!forum/stardog]







