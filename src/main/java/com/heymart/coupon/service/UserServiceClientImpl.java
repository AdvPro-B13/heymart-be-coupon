package com.heymart.coupon.service;
import com.heymart.coupon.dto.UserResponse;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceClientImpl implements UserServiceClient {
    RestTemplate restTemplate = new RestTemplate();
    @Value("${user.api}")
    String userServiceUrl;
    private final UsedCouponRepository usedCouponRepository;
    @Autowired
    public UserServiceClientImpl(UsedCouponRepository usedCouponRepository, RestTemplate restTemplate) {
        this.usedCouponRepository = usedCouponRepository;
        this.restTemplate = restTemplate;
    }

    public boolean verifySupermarket(String token, String supermarketId) {
        if (token == null || supermarketId == null) {
            return false;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            String url = userServiceUrl + "/get";
            ResponseEntity<UserResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserResponse.class
            );
            return supermarketId.equals(Objects.requireNonNull(response.getBody()).getSupermarketId());
        }
        catch(Exception e){
            return false;
        }
    }
    public UsedCoupon useCoupon(String token, UUID couponId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = userServiceUrl + "/get";
        UserResponse response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserResponse.class
        ).getBody();

        assert response != null;
        Long userId = response.getId();
        boolean isExist = usedCouponRepository.existsByUserIdAndCouponId(userId, couponId);
        if (isExist) {
            throw new CouponAlreadyUsedException(ErrorStatus.COUPON_ALREADY_USED.getValue());
        }
        UsedCoupon usedCoupon = new UsedCoupon(couponId, userId);
        return usedCouponRepository.save(usedCoupon);
    }
}
