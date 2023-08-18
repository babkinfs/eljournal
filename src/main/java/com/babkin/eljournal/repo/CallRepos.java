package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRepos extends JpaRepository<Call, Long> {

    Call findByName(String name);

    //Call findById(Long callid);
    Call findCallById(Long callid);

    @Override
    List<Call> findAll();

    @Override
    void delete(Call call);

}
