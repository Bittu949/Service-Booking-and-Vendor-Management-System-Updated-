package com.sqts.sbvms.Repository;

import com.sqts.sbvms.Entity.User;
import com.sqts.sbvms.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    boolean existsByRole(Role role);
}
