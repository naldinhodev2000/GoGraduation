package fafenterprise.dev.gograduation.entity.relationship;


import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Subscription")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue
    private UUID id;


    @JoinColumn(name = "usuario_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "mensalidade_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MonthlyFeeEntity monthlyFee;

    @Column(name = "subscription_date")
    private LocalDateTime subscriptionDate;

    @Column(name = "status")
    private SubscriptionStatus status;




}
