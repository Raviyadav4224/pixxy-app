package com.pixxy.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixxy.user.model.UserModel;

public interface UserRepo extends JpaRepository<UserModel, Long> {

	Optional<UserModel> findByEmail(String email);

}
