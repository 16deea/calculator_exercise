package digital.metro.pricing.calculator;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Repository
public class PriceRepository {

    private final Map<String, BigDecimal> prices = new HashMap<>();
    private final Random random = new Random();

    public BigDecimal getPriceByArticleId(String articleId) {
        return prices.computeIfAbsent(articleId,
                key -> BigDecimal.valueOf(0.5d + random.nextDouble() * 29.50d).setScale(2, RoundingMode.HALF_UP));
    }

    public BigDecimal getPriceByArticleIdAndCustomerId(String id1, String id2) {
        switch (id2) {
            case "customer-1":
                return getPriceByArticleId(id1).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP);
            case "customer-2":
                return getPriceByArticleId(id1).multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP);
        }

        return null;
    }

    public BigDecimal getDiscountByCustomerId(BigDecimal price, String customerId) {
        switch (customerId) {
            case "customer-1":
                return price.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP);
            case "customer-2":
                return price.multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP);
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }
}
