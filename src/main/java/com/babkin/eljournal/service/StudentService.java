package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.repo.GroupRepos;
import com.babkin.eljournal.repo.StudentRepos;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    //24.03.2021

    private GroupRepos groupRepos;
    private StudentRepos studentRepos;

    public StudentService(GroupRepos groupRepos, StudentRepos studentRepos){
        this.groupRepos = groupRepos;
        this.studentRepos = studentRepos;
    }

    public List<Student> findByGrouppIdAndSubGroupp(Groupp group, Long subgroup) {
        if (group == null){
            return  null;
        }
        return  studentRepos.findByGrouppId(group.getId(), Sort.by("lastname", "firstname").ascending());
    }
    public List<Student> findByGrouppId(Groupp group) {
        if (group == null){
            return  null;
        }
        return  studentRepos.findByGrouppId(group.getId(), Sort.by("lastname", "firstname").ascending());
    }
    public Student saveIntoStudent(Firstname firstname, Secondname secondname, Lastname lastname,
                                   Groupp groupp, User user ) {
        if ((lastname != null) && (secondname != null) && (lastname != null)) {
            Student studentFromDB = studentRepos.findByFirstname_IdAndSecondname_IdAndLastname_IdAndGroupp_Id(
                    firstname.getId(), secondname.getId(), lastname.getId(), groupp.getId() );
            if  (studentFromDB == null) {
                Student student = new Student( firstname, secondname, lastname, user, groupp );
                return studentRepos.save( student );
            }
            return studentFromDB;
        }
        return null;
    }

    public Student findByUser(User user){
        if (user == null) {
            return null;
        }
        return studentRepos.findByUser_Id( user.getId() );
    }
}

