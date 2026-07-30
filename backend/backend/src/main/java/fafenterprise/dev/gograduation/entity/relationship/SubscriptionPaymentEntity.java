package fafenterprise.dev.gograduation.entity.relationship;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@Table(name = "subscription_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPaymentEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn (name = "subscription_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SubscriptionEntity subscription;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "reference")
    private YearMonth reference;
}
