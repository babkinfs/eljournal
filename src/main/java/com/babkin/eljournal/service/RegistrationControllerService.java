package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.Role;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationControllerService {

    @Autowired
    private CallService callService;
    @Autowired
    StartdataService startdataService;
    @Autowired
    private LastnameService lastnameService;
    @Autowired
    private FirstnameService firstnameService;
    @Autowired
    private SecondnameService secondnameService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DataSource dataSource;


    public void fixage(Startdata startdata, User user){

        Teacher teacher = new Teacher(startdata.getFirstname(), startdata.getSecondname(), startdata.getLastname(), user, "");
        Set<Role> roles = new HashSet<Role>();
        roles.add( Role.TEACHER  );
        roles.add( Role.STUDENT  );
        user.setRoles( roles );
        teacherService.save( teacher );

    }

    public Startdata isFIOok(String lastname, String firstname, String secondname){
        Lastname lastname1 = lastnameService.findLastnameByFamily( lastname );
        Firstname firstname1 = firstnameService.findFirstnameByName( firstname );
        Secondname secondname1 = secondnameService.findSecondnameByName( secondname );
        if ((lastname1 != null) && (firstname1 != null) && (secondname1 != null)) {
            return startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family( firstname1, secondname1, lastname1 );
        }
        return null;// new Startdata("not", null, null, null, null, 0L);
    }
}
