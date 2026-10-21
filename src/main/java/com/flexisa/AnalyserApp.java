package com.flexisa;

import java.util.List;

import com.flexisa.data.CustomerParser;
import com.flexisa.data.DataLoader;
import com.flexisa.data.LoadResult;
import com.flexisa.data.ProductParser;
import com.flexisa.data.SampleOrders;
import com.flexisa.model.Customer;
import com.flexisa.model.Product;
import com.flexisa.service.SalesAnalyser;

public final class AnalyserApp {

    private AnalyserApp() {
    }

    public static void main(String[] args) {
        LoadResult<Product> productResult = DataLoader.parseAll(
                DataLoader.readResourceLines("products.csv"),
                ProductParser::parseLine);
        List<Product> products = productResult.valid();
        printLoadResult("Products", productResult);

        LoadResult<Customer> customerResult = DataLoader.parseAll(
                DataLoader.readResourceLines("customers.csv"),
                CustomerParser::parseLine);
        List<Customer> customers = customerResult.valid();
        printLoadResult("Customers", customerResult);

        SalesAnalyser analyser = new SalesAnalyser(
                products, SampleOrders.create(customers, products));
        System.out.println(analyser.buildSalesSummary(3));
    }

    private static void printLoadResult(String label, LoadResult<?> result) {
        System.out.println(label + " loaded: " + result.valid().size());
        result.errors().forEach(System.out::println);
    }
}
