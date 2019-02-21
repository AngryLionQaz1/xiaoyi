package com.snow.xiaoyi.common.repository;

import com.snow.xiaoyi.common.pojo.Customer;
import com.vividsolutions.jts.geom.Geometry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    @Query(value = "select c from Customer c where within(c.geom, ?1) = true")
    List<Customer> findWithin(Geometry filter);
}