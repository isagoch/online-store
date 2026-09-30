# Debugging Notes - Week 1
At the Point where the program was to prompt the user to input the location, I called a method (next()) on the scanner object.
It led to a bug because (next()) stops at a delimeter. 
e.g If the user entered Port Harcourt as the location, only "Port" would be read by the program.
so i switch to scanner.nextLine() because It does not skip leading whitespace. It reads everything including spaces until it hits Enter.