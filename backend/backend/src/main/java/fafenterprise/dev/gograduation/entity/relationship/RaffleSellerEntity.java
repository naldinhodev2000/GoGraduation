package fafenterprise.dev.gograduation.entity.relationship;


import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table (name = "raffle_seller")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RaffleSellerEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn (name = "raffle_id")
    @ManyToOne (fetch = FetchType.LAZY)
    private RaffleEntity raffle;

    @JoinColumn (name = "user_id")
    @ManyToOne (fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "quantity")
    private Integer quantity;

    @Column (name = "amount_due")
    protected BigDecimal amountDue;

}
