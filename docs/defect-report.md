# Defect Report: reduceStock rejects exact stock quantity

## Symptoms
The test `reduceStock_QuantityEqualToStock_ReducesStockToZero` failed. When I tried to reduce a product's stock by exactly the amount available (5 out of 5), the program threw an exception instead of reducing the stock to 0.

## Stack trace
```
Caused by: com.flexisa.exception.InsufficientStockException: Insufficient stock to reduce by 5
        at com.flexisa.model.Product.reduceStock(Product.java:53)
        at com.flexisa.model.ProductTest.lambda$reduceStock_QuantityEqualToStock_ReducesStockToZero$0(ProductTest.java:91)
```

## Root cause
The check in `Product.reduceStock` was `quantity >= stockQuantity`. It should be `quantity > stockQuantity`. Reducing by exactly the available stock is allowed, because it just leaves 0. With `>=`, that case was wrongly rejected (an off-by-one error).

## Debugging steps
1. I ran `mvn clean test` and read the stack trace to find the failing line (`Product.java:53`).
2. I set a breakpoint on the `if` condition in `reduceStock` and used Debug Test in VS Code.
3. In the Variables panel, `quantity` and `stockQuantity` were both 5.
4. I added Watch expressions: `quantity >= stockQuantity` was `true` and `quantity > stockQuantity` was `false`.
5. I stepped over with F10 and the program went straight to the `throw` line, which confirmed the condition was the cause.
6. Screenshots are in `docs/debugging/`.

## Correction
I changed `>=` back to `>` in `Product.reduceStock`.

## Regression test
`reduceStock_QuantityEqualToStock_ReducesStockToZero` in `ProductTest`. To be honest, I introduced this defect deliberately for the debugger exercise, and this existing boundary test is the one that caught it. It now passes, and all 188 tests pass.