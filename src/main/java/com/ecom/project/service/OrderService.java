package com.ecom.project.service;

import com.ecom.project.payload.OrderDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
