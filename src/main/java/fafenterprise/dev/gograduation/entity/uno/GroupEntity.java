package fafenterprise.dev.gograduation.entity.uno;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import fafenterprise.dev.gograduation.entity.relationship.GroupUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "grops")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal goal;

    @Column(nullable = false)
    private String team;

    @Column(nullable = false)
    private String course;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GroupUserEntity> members;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<TransactionEntity> transactions;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<MonthlyFeeEntity> monthlyFees;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<ExpenseEntity> expenses;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<RaffleEntity> raffles;
}