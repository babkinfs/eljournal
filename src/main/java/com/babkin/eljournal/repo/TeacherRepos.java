package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface TeacherRepos extends JpaRepository<Teacher, Long> {
    @EntityGraph(attributePaths = { "firstname", "secondname", "lastname", "user" })
    Teacher findTeacherByFirstname_IdAndSecondname_IdAndAndLastname_Id(Long firstnameId, Long secondnameId, Long lastnameId);
    @EntityGraph(attributePaths = { "firstname", "secondname", "lastname", "user" })
    Teacher findTeacherByFirstname_IdAndSecondname_IdAndLastname_IdAndRole(Long firstnameId, Long secondnameId, Long lastnameId, String role);

    @EntityGraph(attributePaths = { "firstname", "secondname", "lastname", "user" })
        //@EntityGraph(attributePaths = {"secondname"})
    Teacher findTeacherByUser(User user);

}
