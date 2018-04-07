#!/bin/bash
for file in test/*.pf
do
	echo "---------------------------------------------------------------------------------------"
	echo $file
	echo "---------------------------------------------------------------------------------------"
	echo "$(cat $file)"
	echo $'\n'
	java -jar ../ASTPrinter.jar $file
	echo ""
done
