# Collection Selection Note

This note explains why I used a `List`, a `Set` and a `Map` in the order and sales analyser, and which one answers each of the three questions that matter: ordering, uniqueness and lookup.

## List

**Where I used it.** I used `List` throughout `SalesAnalyser.java`, starting with the two fields `private final List<Product> products` and `private final List<Order> orders`, which store the data the analyser works with. I also use lists as the return type of most methods, for example:

- `searchProducts()` and `searchOrders()` return the products or orders that match a keyword.
- `filterOrdersByStatus()` returns the orders with a given status.
- `sortProductsByPrice()` returns products sorted by price.
- `removeDuplicateOrders()` returns the orders left after duplicates are removed.

**Why a list was the right choice.** A list keeps its elements in order and allows the same value to appear more than once. Search, filter and sort results are sequences of records, and my sample data really does contain duplicate records (two `P003` rows and two `O007` rows). The analyser has to be able to show those raw records.

**What would have gone wrong with a set.** `Product.equals()` compares by id, so a `Set<Product>` would collapse the two Mechanical Keyboards into one. My price-sort test expects both of them, at different prices, and `searchOrders("O007")` expects two records. A set also has no positions, so a sorted result cannot be read as "the 5th cheapest product".

## Set

**Where I used it.** I used `Set` when I needed to identify or remove duplicates.

- `removeDuplicateProducts()` and `removeDuplicateOrders()` copy the list into a `LinkedHashSet`, which removes duplicates while keeping insertion order. The first record for an id is kept.
- `findDuplicateOrderIds()` uses two sets. `seen` is a `HashSet`, because I only need to check whether an id has already appeared and never read it back in order. `duplicates` is a `LinkedHashSet`, so an id that appears three times is reported once, in the order it was first found.

**Why `equals()` and `hashCode()` matter.** A set decides whether two objects are the same using `equals()` and `hashCode()`. In my first experiment, the set kept two `Product` objects with identical fields because the default `equals()` compares identity, not values. After I defined equality by id in `Product`, `Customer` and `Order`, the set recognises duplicate records.

## Map

**Where I used it.** I used `Map` in `groupOrdersByStatus()` and `groupOrdersByCustomerId()`. In the first, the key is an `OrderStatus` and the value is the list of orders with that status. In the second, the key is a customer id and the value is that customer's list of orders.

**Why a map.** A map goes straight from a key to its group, for example `groups.get("C004")`. With only a list, I would have to loop through every order each time I wanted one customer's orders.

**Why `LinkedHashMap`.** A regular `HashMap` does not guarantee any order, so a printed report could change from run to run. `LinkedHashMap` keeps the keys in the order they were first found, and my tests expect that.

**Missing keys.** A status or customer with no orders has no key at all, rather than an empty list. For example, customer `C007` has no orders and does not appear in the customer groups.

## Summary

| Question | Collection | Reason |
|---|---|---|
| Ordering | `List` | Keeps order and allows repeated records. |
| Uniqueness | `Set` | Refuses repeated elements. It relies on `equals()` and `hashCode()`. |
| Lookup | `Map` | Finds a group directly by its key. |

A list can hold the same item twice and gives each item a position. A set refuses repeats but has no positions. That trade-off is why raw records stay in lists, and duplicates are removed by passing them through a set.