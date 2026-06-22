package fafenterprise.dev.gograduation.entity.uno;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "projected_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExpenseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;


    @Column(nullable = false, length = 100, name = "description")
    private String description;

    @Column(nullable = false, name = "value")
    private BigDecimal value;

}
