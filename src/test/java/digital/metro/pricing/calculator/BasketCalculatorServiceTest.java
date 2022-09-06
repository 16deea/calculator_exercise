package digital.metro.pricing.calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class BasketCalculatorServiceTest {

    @Mock
    private PriceRepository mockPriceRepository;

    private BasketCalculatorService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        service = new BasketCalculatorService(mockPriceRepository);
    }

    @Test
    public void testCalculateArticle() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal price = new BigDecimal("34.29");
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(price);
        Mockito.when(mockPriceRepository.getDiscountByCustomerId(price, null)).thenReturn(price);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), null);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(price);
    }

    @Test
    public void testCalculateArticleForCustomer() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal standardPrice = new BigDecimal("34.29");
        BigDecimal customerPrice = new BigDecimal("29.99");
        String customerId = "customer-1";

        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(standardPrice);
        Mockito.when(mockPriceRepository.getDiscountByCustomerId(standardPrice, customerId)).thenReturn(customerPrice);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), "customer-1");

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(customerPrice);
    }

    @Test
    public void testCalculateBasket() {
        // GIVEN
        String customerId = "customer-1";
        String articleOne = "article-1";
        String articleTwo = "article-2";
        String articleThree = "article-3";

        Basket basket = new Basket(customerId, Set.of(
                new BasketEntry(articleOne, BigDecimal.ONE),
                new BasketEntry(articleTwo, BigDecimal.ONE),
                new BasketEntry(articleThree, BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                articleOne, new BigDecimal("1.50"),
                articleTwo, new BigDecimal("0.29"),
                articleThree, new BigDecimal("9.99"));

        Map<String, BigDecimal> pricesWithDiscount = Map.of(
                articleOne, new BigDecimal("1.12"),
                articleTwo, new BigDecimal("0.11"),
                articleThree, new BigDecimal("8.88"));

        Mockito.when(mockPriceRepository.getPriceByArticleId(articleOne)).thenReturn(prices.get(articleOne));
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleTwo)).thenReturn(prices.get(articleTwo));
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleThree)).thenReturn(prices.get(articleThree));

        Mockito.when(mockPriceRepository.getDiscountByCustomerId(prices.get(articleOne), customerId)).thenReturn(pricesWithDiscount.get(articleOne));
        Mockito.when(mockPriceRepository.getDiscountByCustomerId(prices.get(articleTwo), customerId)).thenReturn(pricesWithDiscount.get(articleTwo));
        Mockito.when(mockPriceRepository.getDiscountByCustomerId(prices.get(articleThree), customerId)).thenReturn(pricesWithDiscount.get(articleThree));

        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        Assertions.assertThat(result.getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(result.getPricedBasketEntries()).isEqualTo(pricesWithDiscount);
        Assertions.assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("10.11"));
    }
}
