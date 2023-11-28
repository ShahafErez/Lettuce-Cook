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

    public User getUserFromToken() {
        Object tokenUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(tokenUser instanceof User)) {
            return null;
        }
        Integer userId = Math.toIntExact(((User) tokenUser).getId());
        return getUserById(userId);
    }

    private User getUserById(Integer id) {
        Optional<User> user = userRepository.findById(Math.toIntExact(id));
        if (!user.isPresent()) {
            String errorMessage = String.format("User %d was not found.", id);
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
        return user.get();
    }
}