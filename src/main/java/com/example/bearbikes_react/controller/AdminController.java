package com.example.bearbikes_react.controller;

import com.example.bearbikes_react.model.Admin;
import com.example.bearbikes_react.repository.AdminsRepository;
import org.springframework.web.bind.annotation.*;

import static com.example.bearbikes_react.utilities.Utils.generateRandomToken;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminsRepository adminsRepository;

    public AdminController(AdminsRepository adminsRepository){
        this.adminsRepository = adminsRepository;
    }

    @GetMapping(value={"/count"})
    public int countRegisteredAdmins(){
        return adminsRepository.countAdmins();
    }

    @PostMapping(value="/register")
    public String registerNewAdmin(
            @RequestParam("email")String email,
            @RequestParam("password")String password,
            @RequestParam("nombre")String nombre
            )
    {
        // http://localhost:9000/admins/register?email=emailAdmin@curl.com&password=constraseña&nombre=adminCurl
        Admin newAdmin = new Admin(email, password, nombre);
        System.out.println(newAdmin);

        adminsRepository.addAdmin(newAdmin);
        return newAdmin.toString();
    }

}
