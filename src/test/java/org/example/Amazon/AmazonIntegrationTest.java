package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {
    Database data;
    ShoppingCartAdaptor cart;
    Amazon amazon;

    @BeforeEach
    void setup() {
        data = new Database();
        data.resetDatabase();
        cart = new ShoppingCartAdaptor(data);
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        amazon = new Amazon(cart, rules);
    }

    @AfterEach
    void close() {
        data.close();
    }

    @Test
    @DisplayName("specification-based")
    void emptyCartZero() {
        double total = amazon.calculate();
        assertEquals(0.0, total, 1e-6);
    }

    @Test
    @DisplayName("structural-based")
    void miscItems() {
        cart.add(new Item(ItemType.OTHER, "Spoons", 2, 6.0));
        cart.add(new Item(ItemType.ELECTRONIC, "Phone", 1, 240.0));
        double total = amazon.calculate();
        assertEquals(252.0 + 5.0 + 7.5, total, 1e-6);
    }
}