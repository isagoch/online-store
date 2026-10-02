# Debugging Notes - Week 1
## Note 1 ##
At the Point where the program was to prompt the user to input the location, I called a method (next()) on the scanner object.
It led to a bug because (next()) stops at a delimeter. 
e.g If the user entered Port Harcourt as the location, only "Port" would be read by the program.
so i switch to scanner.nextLine() because It does not skip leading whitespace. It reads everything including spaces until it hits Enter.

## Note 2 ##
My 3% discount rule wasnt applying because the else-if block ran only the first block of code whose condition is true and then forgot about the rest of the code.
so in order to fix it i made a sperate method specifically for high value discount orders so its checked on every order, wheher or not a quantity tier matched.

## Note 3 ##
 I decided to add several promocodes just to add more variety to the discount rules.

## Note 4 ##
I added the discounts from the original subtotal because it is simpler and more predictable. The percentages just add up, so each customer can see exaclty how much they save.