package com.example.gallery.service;

import com.example.gallery.model.dto.ChargeRequest;
import com.stripe.exception.*;
import com.stripe.model.Charge;

public interface PaymentService {
    Charge pay(ChargeRequest chargeRequest) throws StripeException;
}
