package com.hoxy.hoxymall;

import com.hoxy.hoxymall.entity.User;
import com.hoxy.hoxymall.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HoxymallApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoxymallApplication.class, args);
    }

}
