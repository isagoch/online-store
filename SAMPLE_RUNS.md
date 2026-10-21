# Sample Runs

## Case 1:  A normal order with no promo

**Input:**
```
Price: 276000
Quantity: 5
Location: Abuja
Promo code: (blank)
```

**Output:**
```

Discounted Total for Abuja: #1338600.00
Delivery fee: #0.00
Tax: #26772.00
Total Fee: #1365372.00
```

## Case 2: An order with a promo code.
**Input:**
```
Price: 5000
Quantity: 10
Location: Calabar
Promo code: FLEXISAF
```

**Output:**
```
Discounted Total for Calabar: #47500.00
Delivery fee: #3000.00
Tax: #950.00
Total Fee: #51450.00
```

## Case 3: An order where delivery is free

**Input:**
```
Price: 150000
Quantity: 8
Location: Abia
Promo code: FLEXISA10
```

**Output:**
```
Discounted Total for Abia: #1044000.00
Delivery fee: #0.00
Tax: #20880.00
Total Fee: #1064880.00
```

## Case 4: An invalid input

**Input:**
```
Price: 15000
Quantity: 0
Location: Abuja
Promo code: FLEXISA15
```

**Output:**
```
Input Error: Please input quantity greater than zero
```

## Week 5: AnalyserApp

Run with:

```powershell
mvn clean compile
java -cp target/classes com.flexisa.AnalyserApp
```

Output:

Products loaded: 10
Line 11: Price must be greater than zero
Line 12: For input string: "abc"
Line 13: Invalid product line: P012,Notebook,2500
Customers loaded: 8
Line 9: Customer name must not be blank
Line 10: Email is incorrect
Line 11: Invalid customer line: C010,Lola Adeyemi,lola.adeyemi@example.com,4 Osborne Road,Lagos,Lagos,Nigeria
Orders analysed: 10 (3 records excluded: 1 duplicate, 2 cancelled)
Total sales: #1340500.00
Average order value: #134050.00
Top 3 products by units sold:
P002 Wireless Mouse (10 units)
P006 Desk Lamp (4 units)
P003 Mechanical Keyboard (3 units)

Notes:

- Error line numbers count data lines after the header, so "Line 11" for products is the 11th data row.
- "Products loaded: 10" includes the duplicate P003. Deduplication happens in the analyser.