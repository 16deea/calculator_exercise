package digital.metro.pricing.calculator;

import java.math.BigDecimal;

public class BasketEntry {

    private final String articleId;
    private final BigDecimal quantity;

    public BasketEntry(String articleId, BigDecimal quantity) {
        this.articleId = articleId;
        this.quantity = quantity;
    }

    public String getArticleId() {
        return articleId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
