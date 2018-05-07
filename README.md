# RMLC for RDF Data Cube
Is each column of your statistic CSV file a R2RML mapping? Do you repeat the mapping multiple times changing the 
identifiers and the name of the column? Reduce the complexity of your mappings creating a single RMLC mapping
with an iterator. The properties of the iterator are:
- a global variable where its value will be from 1 to the total number of iterations ($i).
- a local variable to get the name of each column ($i.name). 

Take a look of an example: the [RMLC mapping](https://github.com/dachafra/rmlc-statistic/blob/master/examples/mappings/2016-P21.rmlc.ttl),
the [statistic CSV dataset](https://github.com/dachafra/rmlc-statistic/blob/master/examples/csv/2016-P21.csv) and the [resulted R2RML mapping](https://github.com/dachafra/rmlc-statistic/blob/master/examples/mappings/2016-P21.r2rml.ttl).

## How to run it?
```
git clone https://github.com/dachafra/rmlc-statistic
cd rmlc-statistic
mvn clean install
java -jar target/rmlc-statistic-1.0.jar -m path -c path -s number -e number
```
Where the arguments are:
+ -m: Path to the mapping with iterators. **Mandatory**
+ -c: Path to the CSV file. **Mandatory**
+ -s: The number of the column where the process should start. **Mandatory**
+ -e: The number of the column where the process should end. **Mandatory**

The resulted R2RML mapping will be in the path of the mapping file with the same name and with the extension r2rml.ttl