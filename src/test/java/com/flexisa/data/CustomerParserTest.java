package com.flexisa.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.flexisa.model.Customer;

class CustomerParserTest {

    @Test
    void parseLine_withValidValues_returnsCustomerAndAddressWithExpectedFields() {
        Customer customer = CustomerParser.parseLine(
                "C001,Ada Okafor,ada@example.com,14 Allen Avenue,Ikeja,Lagos,Nigeria,00100");

        assertEquals("C001", customer.getId());
        assertEquals("Ada Okafor", customer.getName());
        assertEquals("ada@example.com", customer.getEmail());
        assertEquals("14 Allen Avenue", customer.getAddress().street());
        assertEquals("Ikeja", customer.getAddress().city());
        assertEquals("Lagos", customer.getAddress().state());
        assertEquals("Nigeria", customer.getAddress().country());
        assertEquals("00100", customer.getAddress().postalCode());
    }

    @Test
    void parseLine_withSpacesAroundValues_trimsValues() {
        Customer customer = CustomerParser.parseLine(
                " C001 , Ada Okafor , ada@example.com , 14 Allen Avenue , Ikeja , Lagos , Nigeria , 00100 ");

        assertEquals("C001", customer.getId());
        assertEquals("Ada Okafor", customer.getName());
        assertEquals("ada@example.com", customer.getEmail());
        assertEquals("14 Allen Avenue", customer.getAddress().street());
        assertEquals("Ikeja", customer.getAddress().city());
        assertEquals("Lagos", customer.getAddress().state());
        assertEquals("Nigeria", customer.getAddress().country());
        assertEquals("00100", customer.getAddress().postalCode());
    }

    @Test
    void parseLine_withTooFewColumns_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> CustomerParser.parseLine(
                        "C001,Ada Okafor,ada@example.com,14 Allen Avenue,Ikeja,Lagos,Nigeria"));

        assertEquals("Invalid customer line: "
                + "C001,Ada Okafor,ada@example.com,14 Allen Avenue,Ikeja,Lagos,Nigeria",
                exception.getMessage());
    }

    @Test
    void parseLine_withNullOrBlankLine_throwsIllegalArgumentException() {
        IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class,
                () -> CustomerParser.parseLine(null));
        IllegalArgumentException blankException = assertThrows(IllegalArgumentException.class,
                () -> CustomerParser.parseLine("   "));

        assertEquals("Customer line must not be blank", nullException.getMessage());
        assertEquals("Customer line must not be blank", blankException.getMessage());
    }

    @Test
    void parseLine_withInvalidEmail_propagatesCustomerValidationMessage() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> CustomerParser.parseLine(
                        "C001,Ada Okafor,ada.example.com,14 Allen Avenue,Ikeja,Lagos,Nigeria,00100"));

        assertEquals("Email is incorrect", exception.getMessage());
    }
}
