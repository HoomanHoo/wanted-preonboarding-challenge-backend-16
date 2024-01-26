package com.wanted.preonboarding.user.infrastructure;

import com.wanted.preonboarding.user.domain.entity.UserInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {

}
