package fafenterprise.dev.gograduation.entity.uno;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "cash_register")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cash {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private BigDecimal value;

    

}