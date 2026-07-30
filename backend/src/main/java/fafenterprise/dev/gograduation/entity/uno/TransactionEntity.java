package fafenterprise.dev.gograduation.entity.uno;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;
import fafenterprise.dev.gograduation.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_id", nullable = false)
    private Cash cashRegister;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Caso a entrada seja referente a uma rifa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffle_id")
    private RaffleEntity raffle;

    // Caso a entrada seja referente ao pagamento de uma mensalidade
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_payment_id")
    private SubscriptionPaymentEntity subscriptionPayment;
}