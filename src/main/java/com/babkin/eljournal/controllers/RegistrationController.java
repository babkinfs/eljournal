package com.babkin.eljournal.controllers;

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.Startdata;
import com.babkin.eljournal.service.RegistrationControllerService;
import com.babkin.eljournal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.*;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationControllerService registrationControllerService;

    @GetMapping("/registration")
    public String registration(){

        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(
            @RequestParam("lastname") String lastname,
            @RequestParam("firstname") String firstname,
            @RequestParam("secondname") String secondname,
            @RequestParam("password2") String passwordConfirm,
            @Valid User user, BindingResult bindingResult, Model model) throws IOException {
        boolean isConfirmEmpty = StringUtils.isEmpty( passwordConfirm );
        if (isConfirmEmpty) {
            model.addAttribute( "password2Error", "Поле для подтвеждения пароля не может быть пустым" );
        }

        if (user.getPassword() != null && !user.getPassword().equals( passwordConfirm )) {
            model.addAttribute( "passwordError", "Пароли не совпадают." );
        }

        boolean isLastnameEmpty = StringUtils.isEmpty( lastname );
        if (isLastnameEmpty) {
            model.addAttribute( "lastnameError", "Поле для фамилии не может быть пустым" );
        }
        boolean isFirstnameEmpty = StringUtils.isEmpty( firstname );
        if (isFirstnameEmpty) {
            model.addAttribute( "firstnameError", "Поле для firstname не может быть пустым" );
        }
        boolean isSscondnameEmpty = StringUtils.isEmpty( secondname );
        if (isSscondnameEmpty) {
            model.addAttribute( "secondnameError", "Поле для secondname не может быть пустым" );
        }

        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = UtilsController.getErrors(bindingResult);
            model.mergeAttributes( errors );
            return "registration";
        }

        Startdata startdata = userService.testUserNames( lastname.trim(), firstname.trim(), secondname.trim() );
        //System.out.println("RegistrationController::addUser: OK");
        if (startdata == null ){
            model.addAttribute( "messageType", "danger" );
            //if (isConfirmFIO.getRole().equals( "not" )) {
            model.addAttribute( "message", "В базе нет данных для вашей регистрации, обратитесь к администратору." );
            //} else {
            //    model.addAttribute( "message", "Регистрация была проведена раньше!" );
            //}
            return "login";
        }


        if (!userService.addUser(user, startdata, false)) {
            model.addAttribute( "usernameError", "Такой пользователь уже есть в базе данных!" );
            return "registration";
        }
        //registrationControllerService.fixage( isConfirmFIO, user );
        model.addAttribute( "messageType", "success" );
        model.addAttribute( "message", "Ожидайте в почте запроса на подтверждение" );
        //return "redirect:/login";
        return "login";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser( code );
        if (isActivated) {
            model.addAttribute( "messageType", "success" );
            model.addAttribute( "message", "Пользователь успешно активирован" );
        } else {
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "Активация не возможна!" );
        }

        return "login";
        //return redirect/login";
    }
}
