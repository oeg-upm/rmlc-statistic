# RMLC Iterator
Is each column of your statistic CSV file a R2RML mapping? Do you repeat the mapping multiple times changing the 
identifiers and the name of the column? Reduce the complexity of your mappings creating a single RMLC mapping
with an iterator. The iterator is a local variable where its value will be the name of each column ($column).

You can define the iterated columns using two different properties in the Logical Table Object:
+ rmlc:columns : where you define the name of the CSV columns where the mapping will iterate. Check an example of a mapping
with this property [here](https://github.com/oeg-upm/rmlc-statistic/blob/master/examples/eurostat/eurostatimmigration-columns.rmlc.ttl) and
the [resulted R2RML mapping](https://github.com/oeg-upm/rmlc-statistic/blob/master/examples/eurostat/eurostatimmigration-columns.r2rml.ttl)
+ rmlc:columnRange : where you define a range between two CSV columns where the mapping will iterate. Check an 
example of a mapping with this property [here](https://github.com/oeg-upm/rmlc-statistic/blob/master/examples/eurostat/eurostatimmigration-range.rmlc.ttl)
and the [resulted R2RML mapping](https://github.com/oeg-upm/rmlc-statistic/blob/master/examples/eurostat/eurostatimmigration-range.r2rml.ttl)

You can also define rmlc:dictionary property whose value must define a correlation between the columns and their alias in JSON format. 
A path to a [file in that format](https://github.com/oeg-upm/rmlc-statistic/blob/master/examples/srilanka-tourism/dictionary.json) is also accepted 
in the mapping. If other word instead of the column name is needed, use the variable {$alias} in any place of the mapping. RMLC use
the dictionary to replace the values.



## How to run it?
```
git clone https://github.com/oeg-upm/rmlc-statistic.git
cd rmlc-statistic
mvn clean install
java -jar target/rmlc-statistic-1.0.jar -m path -c path [-r | -q path]
```
Where the arguments are:
+ -m: Absolute path to the mapping with iterators. **Mandatory**
+ -c: Absolute path to the CSV file. **Mandatory**
+ -r: Run the materialization process.
+ -q: Virtualize the data providing an absolute path to a SPARQL query.


After the execution, you will find the results in the mapping folder.


## Scientific Contribution

Check our paper about RMLC Iterator at: http://dchaves.oeg-upm.net/resources/papers/virtual-semstat-2018/

The paper has been accepted at SemStats workshop co-located with the 17th International Semantic Web Conference (2018). The work will also be published as part of the book “Emerging Topics in Semantic Technologies. ISWC 2018 Satellite Events. E. Demidova, A.J. Zaveri, E. Simperl (Eds.), ISBN: 978-3-89838-736-1, 2018, AKA Verlag Berlin”.
