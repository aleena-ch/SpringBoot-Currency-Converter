package com.currency.converter;

import com.currency.converter.model.Item;
import com.currency.converter.model.UserType;
import com.currency.converter.service.DiscountCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.currency.converter.model.Category.GROCERY;
import static com.currency.converter.model.Category.NON_GROCERY;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CurrencyConverterServiceTest {

    List<Item> items = new ArrayList<>();

    UserType userType;
    @InjectMocks
    private DiscountCalculatorServiceImpl discountCalculatorService;
    @Mock
    private UserType userTypeMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Item firstItem = new Item();
        firstItem.setName("Laptop");
        firstItem.setCategory(NON_GROCERY);
        firstItem.setPrice(BigDecimal.valueOf(100));

        Item secondItem = new Item();
        secondItem.setName("Vegetables");
        secondItem.setCategory(GROCERY);
        secondItem.setPrice(BigDecimal.valueOf(200));
        items.add(firstItem);
        items.add(secondItem);

        userType = new UserType();
        userType.setAffiliate(false);
        userType.setCustomer(false);
        userType.setEmployee(true);
        userType.setTenure(3);
    }

    @Test
    void testCalculateDiscountForEmployee() {

        when(userTypeMock.isEmployee()).thenReturn(true);
        when(userTypeMock.isAffiliate()).thenReturn(false);
        when(userTypeMock.isCustomer()).thenReturn(false);
        when(userTypeMock.getTenure()).thenReturn(0);

        BigDecimal discount = discountCalculatorService.calculateDiscount(items, userType);
        BigDecimal expectedAmount = BigDecimal.valueOf(210);

        assertTrue(discount.compareTo(expectedAmount) == 0,
                "Expected: " + expectedAmount + " but got: " + discount);
    }

    @Test
    void testCalculateDiscountForAffiliate() {
        userType.setAffiliate(true);
        userType.setEmployee(false);
        userType.setTenure(3);

        when(userTypeMock.isEmployee()).thenReturn(false);
        when(userTypeMock.isAffiliate()).thenReturn(true);
        when(userTypeMock.isCustomer()).thenReturn(false);
        when(userTypeMock.getTenure()).thenReturn(0);

        BigDecimal discount = discountCalculatorService.calculateDiscount(items, userType);
        BigDecimal expectedAmount = BigDecimal.valueOf(270);
        assertTrue(discount.compareTo(expectedAmount) == 0,
                "Expected: " + expectedAmount + " but got: " + discount);
    }

    @Test
    void testCalculateDiscountForCustomerWithTenureMoreThan2Years() {

        userType.setAffiliate(false);
        userType.setCustomer(true);
        userType.setTenure(3);

        when(userTypeMock.isEmployee()).thenReturn(false);
        when(userTypeMock.isAffiliate()).thenReturn(false);
        when(userTypeMock.isCustomer()).thenReturn(true);
        when(userTypeMock.getTenure()).thenReturn(3);

        BigDecimal discount = discountCalculatorService.calculateDiscount(items, userType);
        BigDecimal expectedAmount = BigDecimal.valueOf(210);
        assertTrue(discount.compareTo(expectedAmount) == 0,
                "Expected: " + expectedAmount + " but got: " + discount);
    }

    @Test
    void testCalculateDiscountForGroceryItemAndCustomerTenureLessThan2Years() {

        Item firstItem = new Item();
        firstItem.setName("Fruits");
        firstItem.setCategory(GROCERY);
        firstItem.setPrice(BigDecimal.valueOf(100));

        Item secondItem = new Item();
        secondItem.setName("Vegetables");
        secondItem.setCategory(GROCERY);
        secondItem.setPrice(BigDecimal.valueOf(200));
        List<Item> itemList = new ArrayList<>();
        itemList.add(firstItem);
        itemList.add(secondItem);

        userType.setAffiliate(false);
        userType.setCustomer(true);
        userType.setTenure(1);
        BigDecimal discount = discountCalculatorService.calculateDiscount(itemList, userType);

        BigDecimal expectedAmount = BigDecimal.valueOf(285);
        assertTrue(discount.compareTo(expectedAmount) == 0,
                "Expected: " + expectedAmount + " but got: " + discount);
    }
}
