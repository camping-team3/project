package com.camping.erp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    // username으로 블랙리스트 존재 여부 및 사유 확인
    Optional<Blacklist> findByUsername(String username);
}
