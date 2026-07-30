
package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.SubscriptionDTO;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import fafenterprise.dev.gograduation.enums.SubscriptionStatus;
import fafenterprise.dev.gograduation.services.SubscriptionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public SubscriptionEntity subscribe(
            @RequestBody SubscriptionDTO subscriptionDTO) {

        return subscriptionService.subscribe(subscriptionDTO);
    }

    @GetMapping
    public List<SubscriptionEntity> listAll() {

        return subscriptionService.listAll();
    }

    @GetMapping("/group/{groupId}")
    public List<SubscriptionEntity> listByGroup(
            @PathVariable UUID groupId) {

        return subscriptionService.listByGroup(groupId);
    }

    @PutMapping("/{subscriptionId}/status")
    public void changeStatus(
            @PathVariable UUID subscriptionId,
            @RequestBody SubscriptionStatus status) {

        subscriptionService.changeStatus(
                subscriptionId,
                status
        );
    }
}

