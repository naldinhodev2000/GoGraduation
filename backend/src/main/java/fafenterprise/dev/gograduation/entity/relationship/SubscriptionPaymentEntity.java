
package fafenterprise.dev.gograduation.entity.relationship;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subscription_id",
            nullable = false
    )
    private SubscriptionEntity subscription;

    @Column(
            name = "date",
            nullable = false
    )
    private LocalDateTime date;

    @Column(
            name = "value",
            nullable = false,
            precision = 20,
            scale = 2
    )
    private BigDecimal value;

    @Column(columnDefinition = "LONGTEXT")
    private String proofImage;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(
            name = "reference",
            nullable = false
    )
    private String reference;
}
