package com.hust.agriculture;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Role;
import com.hust.agriculture.model.User;
import com.hust.agriculture.repository.RoleRepository;
import com.hust.agriculture.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class AgricultureApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AgricultureApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //        create user role
        Role userRole = new Role();
        userRole.setName("CUSTOMER");
        roleRepository.saveAndFlush(userRole);
        //        create admin role

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
//        roleRepository.saveAndFlush(adminRole);

        //
        User adminAccount = new User();
        adminAccount.setUsername("quannar178");
        String password = passwordEncoder.encode("123456");
        System.out.println("password" + password);
        adminAccount.setPassword(password);
        adminAccount.setAddress("Hoang Mai, HN");
        adminAccount.setFullName("Ba Quan");
        adminAccount.setEmail("quannar178@gmail.com");
        adminAccount.setPhoneNumber("0858519502");
        adminAccount.setStatus(Constant.STATUS_ACTIVE);
        adminAccount.setRole(adminRole);
        userRepository.save(adminAccount);

    }
}
