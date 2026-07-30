
package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestBody SubscriptionDTO subscriptionDTO) {

        subscriptionService.subscribe(subscriptionDTO);

        return ResponseEntity.ok(subscriptionDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID id,
            @RequestBody SubscriptionStatus status) {

        subscriptionService.changeStatus(id, status);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionEntity>> listAll() {
        return ResponseEntity.ok(subscriptionService.listAll());
    }
}