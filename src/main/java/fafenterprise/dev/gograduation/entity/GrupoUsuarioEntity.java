package fafenterprise.dev.gograduation.entity;


import fafenterprise.dev.gograduation.enums.Papel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Grupo_Usuario")
public class GrupoUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;


    @JoinColumn(name = "usuario_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UsuarioEntity usuario;

    @JoinColumn(name = "grupo_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GrupoEntity grupo;


    @Column (name = "data_entrada", nullable = false)
    @NotBlank
    private LocalDate dataEntrada;


    @Column (name = "papel", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotBlank
    private Papel papel;



}
