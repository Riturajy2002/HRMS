package com.np.lms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.lms.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

	Optional<User> findById(String id);
	
	User findByUserId(String user_id);

	Optional<User> findByEmailId(String email_id);

	Optional<User> findByContactNo(Long contact_no);
	
	@Modifying
    @Transactional
    @Query("UPDATE User u SET u.profilePicUrl = :profilePicUrl WHERE u.id = :id")
    void updateProfilePicUrl(@Param("id") String id, @Param("profilePicUrl") String profilePicUrl);

}
