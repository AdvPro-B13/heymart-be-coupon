package com.heymart.coupon.service;

import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.ProductCouponRepository;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CouponServiceTest {

    @Mock
    private ProductCouponRepository productCouponRepository;

    @Mock
    private TransactionCouponRepository transactionCouponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @Test
    public void testCreateOrUpdateProductCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();

        when(productCouponRepository.save(any(ProductCoupon.class))).thenReturn(coupon);
        ProductCoupon savedCoupon = couponService.createOrUpdateProductCoupon(coupon);
        assertThat(savedCoupon).isNotNull();
        verify(productCouponRepository).save(any(ProductCoupon.class));
    }

    @Test
    public void testCreateOrUpdateTransactionCoupon() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);
        TransactionCoupon savedCoupon = couponService.createOrUpdateTransactionCoupon(coupon);
        assertThat(savedCoupon).isNotNull();
        verify(transactionCouponRepository).save(any(TransactionCoupon.class));
    }

    @Test
    public void testDeleteProductCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();

        couponService.deleteProductCoupon(coupon);
        verify(productCouponRepository).delete(coupon);
    }

    @Test
    public void testDeleteTransactionCoupon() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        couponService.deleteTransactionCoupon(coupon);
        verify(transactionCouponRepository).delete(coupon);
    }

    @Test
    public void testFindAllProductCoupons() {
        ProductCoupon coupon1 = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket A")
                .setIdProduct("123A")
                .build();

        ProductCoupon coupon2 = new ProductCouponBuilder()
                .setPercentDiscount(20)
                .setFixedDiscount(10)
                .setMaxDiscount(25)
                .setSupermarketName("Supermarket B")
                .setIdProduct("123B")
                .build();

        when(productCouponRepository.findAll()).thenReturn(Arrays.asList(coupon1, coupon2));
        List<ProductCoupon> coupons = couponService.findAllProductCoupons();
        assertThat(coupons).hasSize(2).extracting("supermarketName").containsExactlyInAnyOrder("Supermarket A", "Supermarket B");
        verify(productCouponRepository).findAll();
    }

    @Test
    public void testFindAllTransactionCoupons() {
        TransactionCoupon coupon1 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket A")
                .setMinTransaction(50)
                .build();

        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(15)
                .setFixedDiscount(10)
                .setMaxDiscount(25)
                .setSupermarketName("Supermarket B")
                .setMinTransaction(100)
                .build();

        when(transactionCouponRepository.findAll()).thenReturn(Arrays.asList(coupon1, coupon2));
        List<TransactionCoupon> coupons = couponService.findAllTransactionCoupons();
        assertThat(coupons).hasSize(2).extracting("supermarketName").containsExactlyInAnyOrder("Supermarket A", "Supermarket B");
        verify(transactionCouponRepository).findAll();
    }

}
