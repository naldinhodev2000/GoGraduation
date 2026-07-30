package fafenterprise.dev.gograduation.entity.uno;

import fafenterprise.dev.gograduation.entity.relationship.GroupUserEntity;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String telefone;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<GroupUserEntity> memberships;

    @OneToMany(mappedBy = "user")
    private List<TransactionEntity> transactions;

    @OneToMany(mappedBy = "user")
    private List<SubscriptionEntity> subscriptions;

    @OneToMany(mappedBy = "user")
    private List<RaffleSellerEntity> raffles;
}