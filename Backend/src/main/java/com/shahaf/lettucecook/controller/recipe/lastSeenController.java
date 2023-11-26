package com.shahaf.lettucecook.controller.recipe;


import com.shahaf.lettucecook.entity.recipe.RecipeRedis;
import com.shahaf.lettucecook.service.recipe.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;

@RestController
@RequestMapping(path = PATH_PREFIX + "/last-seen")
public class lastSeenController {

    @Autowired
    RedisService redisService;

    @GetMapping("/get-all")
    public Iterable<RecipeRedis> getAllRecipesByRedis() {
        return redisService.getAllRecipes();
    }
}
