# Assignment2-SEG2105-RETRIEVED-FINAL

This project builds on the OCSF framework and Phase 1 of the SimpleChat application. The goal was to implement new client and server methods to fill all the specifications of Assignment 2 . 


#Setup Instruction (using Eclipse IDE)
1. Clone the phase 1 of the simpleChat application
2. Add the OCSF file as a framework
3. Check if OCSF is setup as a framework
   - right click on simpleChat folder
   - click on properties
   - click Java Build Path
   - click on projects
   - check in Classpath if OCSF is present
   - if OCSF is not present, repeat setup instruction

#Execution instruction
1. Click on 'run' on the top navigation bar
2. click on run configuration
3. click on EchoServer
   - DEFAULT_PORT is setup to 5555
   - To change the default port, click on arguments and add the wanted port (Port number NEEDS to be an integer)
   - click on run
   - Open server console in Window/Show View/Console (or Alt+Shift+Q, C)
4. Repeat step 1 and 2
5. click on clientConsole
   - click on arguments
   - Set up the arguments --> loginId   hostName   port number
   - LoginId and hostName are Strings and port number is an Integer

6. On eclipse you should see the console and have the possibility to switch between the server and client console





#Credits:

This assignment used the phase 1 of the simpleChat application provided by Professor Hussein Al Osman: https://github.com/uOttawaSEG/simpleChat.git

This assignment used the OCSF framework provide by Professor Hussein Al Osman: https://github.com/husseinalosman/OCSF.git
