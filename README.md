
# 1. About
A modularized application simulating ordering products and using mockneat library (https://www.mockneat.com/) to generate sample data, then validated and converted into one resultant json file. The application also uses email validator and j2html library (https://j2html.com/examples.html) to generate HTML table structure containing order summary sent to the clients via their personal emails. 
LocalDate data couldn't be parsed from json file using gson library in modularized application (issue with java.base module), so additional twin class (in which LocalDate type class field was formatted to String) was introduced to handle this problem.  
 
# 2. How to use it

Download the project: 

```https://github.com/CoderNoOne/App2.git```

and then run the App.main method.

NOTE: There might be some issues with files directory relative path pattern which highly depends on your Operating System. Originally the program is designed to be run on windows platform.
