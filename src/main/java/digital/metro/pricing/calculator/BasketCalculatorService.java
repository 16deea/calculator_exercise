package digital.metro.pricing.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BasketCalculatorService {

    private final PriceRepository priceRepository;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        Map<String, BigDecimal> pricedArticles = basket.getEntries().stream()
                .collect(Collectors.toMap(
                        BasketEntry::getArticleId,
                        entry -> calculateArticle(entry, basket.getCustomerId())));

        BigDecimal totalAmount = pricedArticles.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles, totalAmount);
    }

    public BigDecimal calculateArticle(BasketEntry be, String customerId) {
        BigDecimal articlePrice = priceRepository.getPriceByArticleId(be.getArticleId());
        BigDecimal totalPriceArticles = articlePrice.multiply(be.getQuantity());

        if (customerId != null) {
            totalPriceArticles = priceRepository.getDiscountByCustomerId(totalPriceArticles, customerId);
        }

        return totalPriceArticles;
    }

    //Old version
    public BigDecimal calculateArticles(BasketEntry be, String customerId) {
        String articleId = be.getArticleId();
        BigDecimal quantity = be.getQuantity();

        if (customerId != null) {
            BigDecimal customerPrice = priceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId);
            if (customerPrice != null) {
                return customerPrice.multiply(quantity);
            }
        }

        return priceRepository.getPriceByArticleId(articleId).multiply(quantity);
    }
}
