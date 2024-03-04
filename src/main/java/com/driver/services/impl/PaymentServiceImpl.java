package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Optional<Reservation> r1=reservationRepository2.findById(reservationId);
        if(r1.isEmpty())
        {
            throw new RuntimeException("Reservation ID invalid");
        }
        if(!mode.equals("CARD")&&!mode.equals("UPI")&&!mode.equals("CASH"))
        {
            throw new RuntimeException("Payment mode not detected");
        }
        Reservation reservation=r1.get();
        Spot spot=reservation.getSpot();

        int payable=reservation.getNumberOfHours()*spot.getPricePerHour();
        if(payable>amountSent)
        {
            throw new RuntimeException("Insufficient Amount");
        }
        Payment payment=new Payment();
        payment.setPaymentCompleted(true);
        if(mode.equals("CARD"))
        {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else if(mode.equals("UPI"))
        {
            payment.setPaymentMode(PaymentMode.UPI);
        }
        else{
            payment.setPaymentMode(PaymentMode.CASH);
        }

        payment.setReservation(reservation);
        paymentRepository2.save(payment);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);
        return payment;
    }
}
