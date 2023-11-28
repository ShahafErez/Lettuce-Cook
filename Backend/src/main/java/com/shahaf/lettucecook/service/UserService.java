package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.exceptions.ResourceNotFound;
import com.shahaf.lettucecook.reposetory.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getUserByUsername(String username) {
        logger.info("Fetching user {}.", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            String errorMessage = String.format("User %s was not found.", username);
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
        return user.get();
    }

    public User getUserFromToken() {
        Object tokenUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (tokenUser instanceof User) {
            return ((User) tokenUser);
        }
        return null;
    }
}