package fafenterprise.dev.gograduation.entity.uno;


import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "raffle")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RaffleEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "total")
    private Integer total;

    @OneToMany(mappedBy = "raffle")
    private List<RaffleSellerEntity> sellers;


}
