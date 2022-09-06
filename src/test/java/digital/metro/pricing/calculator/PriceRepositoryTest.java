package digital.metro.pricing.calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceRepositoryTest {

    private final PriceRepository priceRepository = new PriceRepository();

    @Test
    public void getDiscountByCustomerIdForCustomer1() {
        // GIVEN
        BigDecimal price = new BigDecimal("10.00");
        BigDecimal finalPrice = new BigDecimal("9.00");

        // WHEN
        BigDecimal result = priceRepository.getDiscountByCustomerId(price, "customer-1");

        // THEN
        assertEquals(finalPrice, result);
    }

    @Test
    public void getDiscountByCustomerIdForCustomer2() {
        // GIVEN
        BigDecimal price = new BigDecimal("10.00");
        BigDecimal finalPrice = new BigDecimal("8.50");

        // WHEN
        BigDecimal result = priceRepository.getDiscountByCustomerId(price, "customer-2");

        // THEN
        assertEquals(finalPrice, result);
    }

    @Test
    public void getDiscountByCustomerIdForCustomer3() {
        // GIVEN
        BigDecimal price = new BigDecimal("10.00");

        // WHEN
        BigDecimal result = priceRepository.getDiscountByCustomerId(price, "customer-3");

        // THEN
        assertEquals(price, result);
    }
}