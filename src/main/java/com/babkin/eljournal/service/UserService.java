package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.Role;
import com.babkin.eljournal.entity.temporaly.MyMailSender;
import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private LastnameService lastnameService;
    @Autowired
    private FirstnameService firstnameService;
    @Autowired
    private SecondnameService secondnameService;
    @Autowired
    private StartdataService startdataService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private MyMailSender myMailSender;

    //@Autowired
    //private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Value( "${hostname}" )
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername( username );

        if (user == null) {
            throw new UsernameNotFoundException( "Пользователь не найден." );
        }
        return user;
    }

    public Startdata testUserNames(String lastname, String firstname, String secondname){
        Lastname lastname1 = lastnameService.saveIntoLastname( lastname );
        Firstname firstname1 = firstnameService.saveIntoFirstname( firstname );
        Secondname secondname1 = secondnameService.saveIntoSecondname( secondname );
        String first = "отсутствует";
        if (firstname1 != null){
            first = firstname1.getName();
        }
        String second = "отсутствует";
        if (secondname1 != null){
            second = secondname1.getName();
        }
        String last = "отсутствует";
        if (lastname1 != null){
            last = lastname1.getName();
        }
        System.out.println("UserService::testUserNames: " + first + " " + second + " " + last);
        return startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family( firstname1, secondname1, lastname1 );
    }

    public boolean addUser(User user, Startdata startdata, boolean userActive) {
        User userFromDb = userRepo.findByUsername( user.getUsername() );
        if (userFromDb != null){
            if (userFromDb.getActivationCode() != null){
                if (!userActive) {
                    sendMessage(userFromDb);
                }
            }
            return true;
        }
        if (startdata.getRole().equals( "TEACHER" )) {
            Set<Role> roles = new HashSet<Role>();
            roles.add( Role.TEACHER  );
            user.setRoles( roles );
        } else if (startdata.getRole().equals( "LECTOR" )) {
            Set<Role> roles = new HashSet<Role>();
            roles.add( Role.LECTOR  );
            user.setRoles( roles );
        } else {
            user.setRoles( Collections.singleton( Role.STUDENT) );
        }
        user.setActive(userActive);//??false
        user.setActivationCode( UUID.randomUUID().toString() );
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        userRepo.save( user );
        if (startdata.getRole().equals( "TEACHER" ) || startdata.getRole().equals( "LECTOR" )) {
            Teacher teacher = new Teacher( startdata.getFirstname(), startdata.getSecondname(),
                    startdata.getLastname(), user, null );
            teacherService.update( teacher );
        } else {
            //Student student = new Student(startdata.getFirstname(), startdata.getSecondname(), startdata.getLastname(),
            //        user, startdata.getSubGroupp(), startdata.getGroupp());
            studentService.saveIntoStudent( startdata.getFirstname(), startdata.getSecondname(), startdata.getLastname(),
                    startdata.getGroupp(), user );
        }
        if (!userActive) {
            sendMessage(user);
        }
        return true;
    }

    private void sendMessage(User user) {
        //if (!StringUtils.isEmpty( user.getEmail() )) {
        String username = user.getUsername();
        if (!StringUtils.isEmpty( username )) {
            String message = String.format(
                    "Приветствую Вас, %s!  \n" +
                            "Для регистрации, пожалуйста, перейдите по следующей ссылке: http://%s/activate/%s",
                    username,
                    hostname,
                    user.getActivationCode()
            );
            //mailSender.send( user.getEmail(), "Activation code", message );
            System.out.println("UserService::sendMessage: message=" + message);
            if (hostname.equals( "localhost:8080" )){
                myMailSender.send( username, "Activation code", message );
            } else {
                myMailSender.send( username, "Activation code", message );
            }
            System.out.println("UserService::sendMessage: После username=" + username);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        user.setActivationCode( null );
        userRepo.save( user );
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername( username );

        Set<String> roles = Arrays.stream( (Role.values()) )
                .map( (Role::name) )
                .collect( Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()){
            if (roles.contains( key )){
                user.getRoles().add( Role.valueOf( key ) );
            }
        }
        userRepo.save( user );

    }

    public void updateProfile(User user, String password){//, String email) {
    }

    public User findUserById(Long id){
        return userRepo.findUserById( id );
    }

    public User findByUsername(String username){
        return userRepo.findByUsername( username );
    }

    public User save(User user){
        User user1 = userRepo.findByUsername( user.getUsername() );
        if (user1 == null) {
            return userRepo.save( user );
        }
        return user1;
    }
}
