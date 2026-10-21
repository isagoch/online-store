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

```text
(paste the output here with Ctrl+V)
```

Notes:

- Error line numbers count data lines after the header, so "Line 11" for products is the 11th data row.
- "Products loaded: 10" includes the duplicate P003. Deduplication happens in the analyser.