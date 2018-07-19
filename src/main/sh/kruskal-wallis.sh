#!/bin/sh

# check for xsv
if ! [ -x "$(command -v xsv)" ]
then
    echo "Error: xsv is not installed (install using cargo)"
    exit 1
fi

# check for Rscript
if ! [ -x "$(command -v Rscript)" ]
then
    echo "Error: Rscrpt is not installed"
    exit 1
fi

# check for two file arguments
if [ $# -ne 2 ]
then
    echo "Usage: $0 <csv-file1> <csv-file2>"
    exit 1
fi

if ! [ -f $1 ]
then
    echo "Error: file '$1' does not exist" 
    exit 1
fi

if ! [ -f $2 ]
then
    echo "Error: file '$2' does not exist" 
    exit 1
fi

# check for field count equality between files
ARG1_FIELDS=`head -n 1 $1 | awk -F "," '{print NF-1}'`
ARG2_FIELDS=`head -n 1 $2 | awk -F "," '{print NF-1}'`

if [ $ARG1_FIELDS -ne $ARG2_FIELDS ]
then
    echo "Error: files contain a different number of fields"
    exit 1
fi

# iterate over variables performing kruskal wallis on each
for i in $(seq 1 $ARG1_FIELDS)
do
    echo "group,value" > tmp.csv
    xsv select $i $1 | awk '{print "1," $0}' >> tmp.csv
    xsv select $i $2 | awk '{print "2," $0}' >> tmp.csv

    RESULT=`Rscript ../r/kruskal-wallis.r | grep "Kruskal-Wallis"`
    echo $RESULT | awk '{print "chi-squared: " $8 " p-value: " $14}' 
done

rm tmp.csv
