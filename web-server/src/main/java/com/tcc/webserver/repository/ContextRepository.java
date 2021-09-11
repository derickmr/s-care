package com.tcc.webserver.repository;

import com.tcc.webserver.models.Context;
import com.tcc.webserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

    List<Context> getAllByUserOrderByDate(User user);

}
