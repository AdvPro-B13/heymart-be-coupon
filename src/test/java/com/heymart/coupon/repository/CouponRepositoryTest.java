package com.heymart.coupon.repository;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.builder.CouponBuilder;
import com.heymart.coupon.model.enums.CouponType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CouponRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void testSaveCoupon() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        Coupon savedCoupon = couponRepository.save(coupon);
        assertNotNull(savedCoupon);
        assertEquals("PRODUCT", savedCoupon.getType());
    }
    @Test
    public void testFindAllCoupon() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        List<Coupon> couponList = couponRepository.findAll();
        assertEquals(couponList.size(),0);
        Coupon savedCoupon = couponRepository.save(coupon);
        couponList = couponRepository.findAll();
        assertEquals(couponList.size(),1);
    }
    @Test
    public void testFindByIdCoupon() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();


        couponRepository.save(coupon);
        Coupon couponFound = couponRepository.findById(coupon.getId()).get();
        assertEquals(coupon.getId(),couponFound.getId());
    }
    @Test
    public void testFindByIdCouponNotExist() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        couponRepository.save(coupon);
        assertThrows(NoSuchElementException.class, () -> {
            Coupon couponFound = couponRepository.findById("NOT_VALID_ID").get();
        });
    }
    @Test
    public void testDeleteCouponSuccess() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        Coupon savedCoupon = couponRepository.save(coupon);
        assertEquals(couponRepository.findAll().size(),1);
        couponRepository.delete(coupon);
        assertEquals(couponRepository.findAll().size(),0);
    }
    @Test
    public void testDeleteCouponNotExist() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        Coupon coupon2 = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        couponRepository.save(coupon);
        assertEquals(couponRepository.findAll().size(),1);
        couponRepository.delete(coupon2);
        assertEquals(couponRepository.findAll().size(),1);
    }
    @Test
    public void testUpdateCoupon() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        Coupon savedCoupon = couponRepository.save(coupon);
        assertEquals(savedCoupon.getMaxDiscount(), 300);
        coupon.setMaxDiscount(250);
        couponRepository.save(coupon);
        Coupon updatedCoupon = couponRepository.findById(coupon.getId()).orElseThrow();
        assertEquals(updatedCoupon.getMaxDiscount(), 250);
    }
}
