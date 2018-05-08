# RMLC Iterator
Is each column of your statistic CSV file a R2RML mapping? Do you repeat the mapping multiple times changing the 
identifiers and the name of the column? Reduce the complexity of your mappings creating a single RMLC mapping
with an iterator. The iterator is a local variable where its value will be the name of each column ($name).

You can define the iterated columns using two different properties:
+ rmlc:columns : where you define the name of the CSV columns where the mapping will iterate. Check an example of a mapping
with this property [here](https://github.com/dachafra/rmlc-statistic/blob/master/examples/mappings/2016-P21-columns.rmlc.ttl) and
the [resulted R2RML mapping](https://github.com/dachafra/rmlc-statistic/blob/master/examples/mappings/2016-P21-columns.r2rml.ttl)
+ rmlc:columnRange : where you define a range between two CSV columns where the mapping will iterate. Check an 
example of a mapping with this property [here](https://github.com/dachafra/rmlc-statistic/blob/master/examples/mappings/2016-P21-range.rmlc.ttl)
and the [resulted R2RML mapping](https://github.com/dachafra/rmlc-statistic/blob/master/examples/mappings/2016-P21-range.r2rml.ttl)



## How to run it?
```
git clone https://github.com/dachafra/rmlc-statistic
cd rmlc-statistic
mvn clean install
java -jar target/rmlc-statistic-1.0.jar -m path -c path
```
Where the arguments are:
+ -m: Path to the mapping with iterators. **Mandatory**
+ -c: Path to the CSV file. **Mandatory**

The resulted R2RML mapping will be in the path of the mapping file with the same name but with an r2rml.ttl extension