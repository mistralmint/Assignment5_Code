package org.example.Barnes;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

import java.util.Map;

class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based")
    void nullOrderIsNull() {
        BookDatabase data = Mockito.mock(BookDatabase.class);
        BuyBookProcess process = Mockito.mock(BuyBookProcess.class);
        BarnesAndNoble barnes = new BarnesAndNoble(data, process);
        assertNull(barnes.getPriceForCart(null));
   }

   @Test
   @DisplayName("specification-based")
   void hasStock() {
        BookDatabase data = Mockito.mock(BookDatabase.class);
        BuyBookProcess process = Mockito.mock(BuyBookProcess.class);
        BarnesAndNoble barnes = new BarnesAndNoble(data, process);
        Book book = new Book("1234", 12, 5);

        Mockito.when(data.findByISBN("1234")).thenReturn(book);

        PurchaseSummary summary = barnes.getPriceForCart(Map.of("1234", 2));

        assertEquals(24, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
        Mockito.verify(process).buyBook(book, 2);
   }

   @Test
   @DisplayName("structural-based")
   void insufficientStock() {
        BookDatabase data = Mockito.mock(BookDatabase.class);
        BuyBookProcess process = Mockito.mock(BuyBookProcess.class);
        BarnesAndNoble barnes = new BarnesAndNoble(data, process);
        Book book = new Book("1234", 12, 3);

        Mockito.when(data.findByISBN("1234")).thenReturn(book);

        PurchaseSummary summary = barnes.getPriceForCart(Map.of("1234", 5));
        assertEquals(36, summary.getTotalPrice());
        assertEquals(1, summary.getUnavailable().size());
        assertEquals(2, summary.getUnavailable().get(book));
        Mockito.verify(process).buyBook(book, 3);
   }


   @Test
   @DisplayName("structural-based")
   void multipleBooks() {
        BookDatabase data = Mockito.mock(BookDatabase.class);
        BuyBookProcess process = Mockito.mock(BuyBookProcess.class);
        BarnesAndNoble barnes = new BarnesAndNoble(data, process);
        Book book1 = new Book("ABC", 5, 12);
        Book book2 = new Book("DEF", 7, 12);

        Mockito.when(data.findByISBN("ABC")).thenReturn(book1);
        Mockito.when(data.findByISBN("DEF")).thenReturn(book2);

        PurchaseSummary summary = barnes.getPriceForCart(Map.of("ABC", 2, "DEF", 1));
        assertEquals(10 + 7, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
        Mockito.verify(process).buyBook(book1, 2);
        Mockito.verify(process).buyBook(book2, 1);
    }
}