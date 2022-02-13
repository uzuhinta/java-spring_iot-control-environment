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
        Role customerRole = new Role();
        customerRole.setName("CUSTOMER");

        User customerAccount = new User();
        customerAccount.setUsername("quannar1781");
        String password = passwordEncoder.encode("123456");
        customerAccount.setPassword(password);
        customerAccount.setAddress("Hoang Mai, HCUSTOMERN");
        customerAccount.setFullName("Ba QuanCUSTOMER");
        customerAccount.setEmail("quannar1781@gmail.com");
        customerAccount.setPhoneNumber("0858519");
        customerAccount.setStatus(Constant.STATUS_ACTIVE);
        customerAccount.setRole(customerRole);
        userRepository.save(customerAccount);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
//        roleRepository.saveAndFlush(adminRole);

        //
        User adminAccount = new User();
        adminAccount.setUsername("quannar178");
        System.out.println("password" + password);
        adminAccount.setPassword(password);
        adminAccount.setAddress("Hoang Mai, HN");
        adminAccount.setFullName("Ba Quan");
        adminAccount.setEmail("quannar178@gmail.com");
        adminAccount.setPhoneNumber("0858519502");
        adminAccount.setStatus(Constant.STATUS_ACTIVE);
        adminAccount.setRole(adminRole);
        userRepository.save(adminAccount);

        Role techniqueRole = new Role();
        techniqueRole.setName("TECHNIQUE");
//        roleRepository.saveAndFlush(techniqueRole);

        User techniqueAccount = new User();
        techniqueAccount.setUsername("quannar1782");
        System.out.println("password" + password);
        techniqueAccount.setPassword(password);
        techniqueAccount.setAddress("Hoang Mai, TECHNIQUE");
        techniqueAccount.setFullName("Ba QuanTECHNIQUE");
        techniqueAccount.setEmail("quannar1782@gmail.com");
        techniqueAccount.setPhoneNumber("085");
        techniqueAccount.setStatus(Constant.STATUS_ACTIVE);
        techniqueAccount.setRole(techniqueRole);
        userRepository.save(techniqueAccount);

    }
}
