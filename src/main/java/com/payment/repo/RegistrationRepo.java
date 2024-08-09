package com.payment.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.payment.entity.UsersTable;


public interface RegistrationRepo extends JpaRepository<UsersTable, Integer> {
@Query(nativeQuery = true,value="select * from  users_table where (phone_Number=:phoneNumber) or (email=:email)")
	UsersTable findByPhoneNumberAndEmail(String phoneNumber, String email);

UsersTable findByEmail(String email);
}
