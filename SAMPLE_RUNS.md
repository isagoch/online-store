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