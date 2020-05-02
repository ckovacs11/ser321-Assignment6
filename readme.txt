Author: Curtis Kovacs (ckovacs1@asu.edu), ASU Polytechnic, CIDSE, SE
Version: April 2020

See http://pooh.poly.asu.edu/Ser321

Purpose: provide basis for assignment 6 solution for Ser321 Spring 2020.
This sample contains open source source code that you may use in generating
student solutions. It also contains in compiled form code that is not
open source, but is freely availble through download in the US. This includes
the classes to manipulate JSON, Gluon's JavaFX, and the instructor's view
user-interface for the assignment. You will need to register with
OMDb to obtain an api key to get this example to run properly.
See: https://www.omdbapi.com/apikey.aspx

This project is designed to run on either Debian Buster Linux or MacOS. It
requires jdk13.

Building and running the sample is done with Ant.
This example depends on the following frameworks:
1. Ant
   see: http://ant.apache.org/
2. Json for Java as implemented (reference) by Doug Crockford.
   See: https://github.com/stleary/JSON-java
3. Gluon's JavaFX for Java13 on Linux.
   See: https://gluonhq.com/products/javafx/

To build and run the example, you will need to have Ant installed on
your system, with the antlib target extensions.
(see the CppFraction example from unit1 material for installing these in your home directory).


To clean the project directory:
ant clean

To run server:
ant server -Dport=1099
To run client:
ant java.client -Dhost=localhost -Dport=1099 -DuserId="name" -DomdbKey="key"

The project directory  includes docs directory containing the javadocs
for the instructor provided software for the user-interface view,
which you will use in creating your solution.

end
