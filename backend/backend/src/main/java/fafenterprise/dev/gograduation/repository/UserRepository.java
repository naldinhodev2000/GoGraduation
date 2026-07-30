package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByLoginOrEmail(String login, String email);
}
