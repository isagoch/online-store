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

## Note 5 ##
While creating a test case for the validateOrder method. I typed in that the expected result should be "Quantity must be positive", forgetting that the message the method throws is "Please input quantity greater than zero". So it caused a failure in the test run. But I fixed it by changing the expected return of the test case to the correct message in the original method.

## Note 6 ##
In the calculateDeliveryFee method I found out that if a location with a white space after it was inputted, it would give a wrong result. So I added the .trim() method to the location variable to trim out any unwanted whitespaces at the beginning and end.

# Debugging Notes - Week 3
## Note 1 ##
After moving classes into new packages, VS Code showed red squiggly lines in several files even though mvn test said BUILD SUCCESS.
The problem was that the Java language server in VS Code had an old cached copy of my project, so it did not know the new packages existed yet.
So i ran "Java: Clean Java Language Server Workspace" and chose "Restart and delete", and the red lines went away. I learnt that Maven is the one to trust when VS Code and Maven disagree.

## Note 2 ##
After running that clean-up, mvn test failed with "cannot find symbol" for classes like PricingContext and HighValueDiscountPolicy, even though the files were really there.
The cause was that the target folder was out of sync with my source files, and Maven wrongly thought it was already up to date.
So i ran mvn clean test, which deletes the target folder and rebuilds everything from scratch, and it passed.

## Note 3 ##
When i pasted my import lines into App.java before writing the code that used them, VS Code deleted them as soon as i saved.
This happens because VS Code removes unused imports on save.
So i added each import only after writing the code that needs it.

## Note 4 ##
After moving PricingContext from the service package to the model package, my code stopped compiling because of two import mistakes.
In PricingService i typed com.flexisa.shippingShippingCalculator and forgot the dot between shipping and ShippingCalculator.
In App.java i also changed the import of PricingService to the model package by mistake, but only PricingContext had moved.
I found both by reading the first [ERROR] line from mvn clean test, and then fixed the imports.

## Note 5 ##
When i moved the printing into a printSummary method, i accidentally changed the output. I added an "Order Summary:" header, removed the # sign and changed the wording of some lines.
The tests still passed because none of them check the printed text, so it only showed up when i compared the output line by line against the Week 1 format.
So i copied the original four println lines back unchanged. A refactor should not change what the user sees.

## Note 6 ##
PricingService imported the discount and shipping packages, and those two packages imported PricingContext from the service package. That means the packages depended on each other in a circle.
I fixed it by moving PricingContext into the model package, so the dependencies now only go one way, towards model.

# Debugging Notes - Week 4
## Note 1 ##
When formatting money in the sales summary, using String.format without a locale could print a comma instead of a decimal point on machines with different default locales.
I used Locale.ROOT with String.format so the report consistently shows two decimal places with a period.

## Note 2 ##
The sales analyser tests and console entry point both load product and customer resources with DataLoader and their parsers.
I kept the small amount of loading code in both places rather than introducing a shared loader abstraction just for this step.

## Note 3 ##
My first test used a HashSet with two Product objects that had the same id, name and price. I guessed equals() would be true, but it printed false and the set size was 2.
This is because Java's default equals() compares object identity, not the values inside.
So i wrote equals() and hashCode() using the id only. I left stock out of hashCode because it changes, and a changed hash would stop the set from finding the product.

## Note 4 ##
My scratch test failed with "cannot find symbol" for Set and HashSet.
The cause was that VS Code deleted my imports on save, because nothing used them yet.
So i wrote the code first and added the imports after.

## Note 5 ##
I wrote assertNotEquals(null, product) to test equals(null), but it never called my equals method, so the test proved nothing.
So i changed it to assertFalse(product.equals(null)), which really calls the method.

## Note 6 ##
VS Code showed a yellow warning on my assertThrows lines, saying the method result was ignored.
The cause was that assertThrows returns the exception and i was not using it.
So i saved the exception and checked its message. That made the tests stronger too.

## Note 7 ##
My scripted menu test printed "Unknown option: [INFO]" many times.
I had typed mvn clean compile and the inputs on one line. Maven read the inputs as part of its command and failed, and the pipe sent Maven's error text into my program as menu choices.
So i ran compile and the program as two separate commands.

# Debugging Notes - Week 5

### Note 1
- Before debugging, I predicted that quantity would be 5 and stockQuantity would be 5, and that the condition would be true.
- What I actually saw:  both were 5 and the >= condition was true, so it jumped to the throw.
- What I learned: a boundary test (exactly equal to the limit) catches mistakes that normal failure tests miss.

### Note 2
- I noticed that logging the exception object prints its stack trace. For a missing file, the exception message contains the full path, which has my Windows username.
- I wrote a test that captures the log records and checks the path is not there. Then I changed the log to print only the exception class name.