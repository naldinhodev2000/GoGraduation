package fafenterprise.dev.gograduation.entity.uno;


import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "monthly_fee")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyFeeEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column (name = "value", nullable = false, precision = 20, scale = 2)
    private BigDecimal value;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "due_date")
    private Integer dueDate;


    @OneToMany(mappedBy = "monthlyFee")
    private List<SubscriptionEntity> subscription;
}
