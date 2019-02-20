package com.snow.xiaoyi.common.repository;

import com.snow.xiaoyi.common.pojo.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {


 Optional<Authority> findByUri(String uri);




}

