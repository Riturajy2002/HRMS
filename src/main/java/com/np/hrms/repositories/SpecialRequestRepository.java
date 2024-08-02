package com.np.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.np.lms.entities.SpecialRequest;

public interface SpecialRequestRepository extends JpaRepository<SpecialRequest, Long> {

	@Query("SELECT sr FROM SpecialRequest sr WHERE sr.specialRequestStatus = 0 AND sr.recipient = :designation")
	List<SpecialRequest> findUnreadSpecialRequests(@Param("designation") String designation );

}
