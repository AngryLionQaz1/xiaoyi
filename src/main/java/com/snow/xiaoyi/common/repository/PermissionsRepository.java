package com.snow.xiaoyi.common.repository;

import com.snow.xiaoyi.common.pojo.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionsRepository extends JpaRepository<Permissions,Long> {


    Optional<Permissions> findByUri(String url);
}
