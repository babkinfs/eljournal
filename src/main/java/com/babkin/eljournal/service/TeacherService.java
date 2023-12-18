package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.Firstname;
import com.babkin.eljournal.entity.working.Lastname;
import com.babkin.eljournal.entity.working.Secondname;
import com.babkin.eljournal.entity.working.Teacher;
import com.babkin.eljournal.repo.TeacherRepos;
import com.babkin.eljournal.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private TeacherRepos teacherRepos;
    private UserRepo userRepo;
    private FirstnameService firstnameService;
    private SecondnameService secondnameService;
    private LastnameService lastnameService;

    public TeacherService(TeacherRepos teacherRepos, UserRepo userRepo,
                          FirstnameService firstnameService, SecondnameService secondnameService, LastnameService lastnameService) {
        this.teacherRepos = teacherRepos;
        this.userRepo = userRepo;
        this.firstnameService = firstnameService;
        this.secondnameService = secondnameService;
        this.lastnameService = lastnameService;
    }

    public Teacher save(Teacher teacher){
        if (teacher != null) {
            Teacher teacherFromDB = teacherRepos.findTeacherByFirstname_IdAndSecondname_IdAndLastname_IdAndRole(
                    teacher.getFirstname().getId(), teacher.getSecondname().getId(),
                    teacher.getLastname().getId(),  teacher.getRole());
            if (teacherFromDB == null) {
                teacherFromDB = teacherRepos.save( teacher );
            }
            return teacherFromDB;
        }
        return null;
    }

    public Teacher findByFirstname_NameAndSecondname_NameAndLastname_Family(String firstname, String secondname, String lastname){
        if ((firstname == null) || (secondname == null) || (lastname == null)){
            return null;
        }
        Firstname firstname1 = firstnameService.findFirstnameByName( firstname );
        Secondname secondname1 = secondnameService.findSecondnameByName( secondname );
        Lastname lastname1 = lastnameService.findLastnameByFamily( lastname );
        //return teacherRepos.findByFirstname_NameAndSecondname_NameAndLastname_Name(
        //        firstname, secondname, lastname );
        if (firstname1 == null || secondname1 == null || lastname1 == null){
            return null;
        }
        return teacherRepos.findTeacherByFirstname_IdAndSecondname_IdAndAndLastname_Id( firstname1.getId(), secondname1.getId(), lastname1.getId() );
    }

    public Teacher findTeacherByUser(User user){
        if (user == null){
            return null;
        }
        return teacherRepos.findTeacherByUser( user );
    }

    public Teacher update(Teacher teacher){
        Teacher fromDb = teacherRepos.findTeacherByFirstname_IdAndSecondname_IdAndAndLastname_Id(
                teacher.getFirstname().getId(), teacher.getSecondname().getId(), teacher.getLastname().getId());
        if (fromDb != null) {
            fromDb.setUser( teacher.getUser() );
            return teacherRepos.saveAndFlush( fromDb );
        }
        //return teacher;
        return null;
    }
}
