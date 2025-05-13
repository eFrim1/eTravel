package com.etravel.userservice.infrastructure;

import com.etravel.userservice.domain.contracts.UserRepository;
import com.etravel.userservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryImpl extends UserRepository, JpaRepository<User, Long> {
}
