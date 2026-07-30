
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

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;
import fafenterprise.dev.gograduation.services.SubscriptionPaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscription-payments")
@RequiredArgsConstructor
public class SubscriptionPaymentController {

    private final SubscriptionPaymentService paymentService;

    /*
     * Registra um pagamento.
     *
     * POST /subscription-payments/{subscriptionId}
     */
    @PostMapping("/{subscriptionId}")
    public SubscriptionPaymentEntity create(
            @PathVariable UUID subscriptionId,
            @RequestBody SubscriptionPaymentEntity payment) {

        return paymentService.create(
                subscriptionId,
                payment
        );
    }

    /*
     * Lista pagamentos de uma assinatura.
     *
     * GET /subscription-payments/subscription/{subscriptionId}
     */
    @GetMapping("/subscription/{subscriptionId}")
    public List<SubscriptionPaymentEntity> listBySubscription(
            @PathVariable UUID subscriptionId) {

        return paymentService
                .listBySubscription(subscriptionId);
    }

    /*
     * Lista todos os pagamentos de uma sala.
     *
     * GET /subscription-payments/group/{groupId}
     */
    @GetMapping("/group/{groupId}")
    public List<SubscriptionPaymentEntity> listByGroup(
            @PathVariable UUID groupId) {

        return paymentService
                .listByGroup(groupId);
    }

    /*
     * Exclui um pagamento.
     *
     * DELETE /subscription-payments/{paymentId}
     */
    @DeleteMapping("/{paymentId}")
    public void delete(
            @PathVariable UUID paymentId) {

        paymentService.delete(paymentId);
    }
}
