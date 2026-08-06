
package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.response.SubscriptionPaymentResponseDTO;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;
import fafenterprise.dev.gograduation.services.SubscriptionPaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscription-payments")
@RequiredArgsConstructor
public class SubscriptionPaymentController {

    private final SubscriptionPaymentService paymentService;

    @PostMapping("/{subscriptionId}")
    public SubscriptionPaymentEntity create(
            @PathVariable UUID subscriptionId,
            @RequestBody SubscriptionPaymentEntity payment) {

        return paymentService.create(
                subscriptionId,
                payment);
    }

    @GetMapping("/subscription/{subscriptionId}")
    public List<SubscriptionPaymentEntity> listBySubscription(
            @PathVariable UUID subscriptionId) {

        return paymentService
                .listBySubscription(subscriptionId);
    }

    @GetMapping("/group/{groupId}")
    public List<SubscriptionPaymentResponseDTO> listByGroup(
            @PathVariable UUID groupId) {

        return paymentService.listByGroup(groupId);

    }

    @DeleteMapping("/{paymentId}")
    public void delete(
            @PathVariable UUID paymentId) {

        paymentService.delete(paymentId);
    }
}
