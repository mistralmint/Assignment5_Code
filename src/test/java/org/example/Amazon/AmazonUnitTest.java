package org.example.Amazon;

import static org.junit.jupiter.api.Assertions.*;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class AmazonUnitTest {

    @Test
    @DisplayName("specification-based")
    void priceRuleCalculations() {
        ShoppingCart cart = mock(ShoppingCart.class);
        when(cart.getItems()).thenReturn(List.of());
        PriceRule rule1 = mock(PriceRule.class);
        PriceRule rule2 = mock(PriceRule.class);
        when(rule1.priceToAggregate(any())).thenReturn(12.0);
        when(rule2.priceToAggregate(any())).thenReturn(2.5);
        Amazon amazon = new Amazon(cart, List.of(rule1, rule2));

        double total = amazon.calculate();
        assertEquals(14.5, total, 1e-6);
        verify(rule1).priceToAggregate(any());
        verify(rule2).priceToAggregate(any());
    }

    @Test
    @DisplayName("structural-based")
    void regularCost() {
        RegularCost cost = new RegularCost();
        List<Item> items = List.of(new Item(ItemType.OTHER, "ABC", 2, 5.0),
                new Item(ItemType.ELECTRONIC, "DEF", 1, 25.0)
        );
        assertEquals(35.0, cost.priceToAggregate(items), 1e-6);
    }

    @Test
    @DisplayName("structural-based")
    void deliveryPriceLists() {
        DeliveryPrice price = new DeliveryPrice();

        assertEquals(0.0,  price.priceToAggregate(List.of()), 1e-6);
        assertEquals(5.0,  price.priceToAggregate(List.of(dummy())), 1e-6);
        assertEquals(5.0,  price.priceToAggregate(List.of(dummy(), dummy(), dummy())), 1e-6);
        assertEquals(12.5, price.priceToAggregate(List.of(dummy(), dummy(), dummy(), dummy())), 1e-6);
        assertEquals(12.5, price.priceToAggregate(List.of(dummy(), dummy(), dummy(), dummy(), dummy(), dummy(),
                dummy(), dummy(), dummy(), dummy())), 1e-6);
        assertEquals(20.0, price.priceToAggregate(List.of(dummy(), dummy(), dummy(), dummy(), dummy(), dummy(),
                dummy(), dummy(), dummy(), dummy(), dummy())), 1e-6);
    }

    private static Item dummy() {
        return new Item(ItemType.OTHER, "n", 1, 1.0);
    }

    @Test
    @DisplayName("structural-based")
    void electronicsExtraCost() {
        ExtraCostForElectronics cost = new ExtraCostForElectronics();

        List<Item> nothing = List.of(new Item(ItemType.OTHER, "Spoon", 1, 1.0));
        assertEquals(0.0, cost.priceToAggregate(nothing), 1e-6);

        List<Item> addedElectronics = List.of(new Item(ItemType.OTHER, "Spoon", 1, 1.0),
                new Item(ItemType.ELECTRONIC, "Charger", 1, 15.0)
        );
        assertEquals(7.5, cost.priceToAggregate(addedElectronics), 1e-6);
    }
}