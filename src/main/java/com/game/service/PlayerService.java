package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.List;

public interface PlayerService {
    void save(Player player);

    void deleteById(long id);

    Player findById(long id);


    List<Player> findAll(int pageNumber, int size, String order);

    public List<Player> findAllByParams(String name,
                                        String title, String race, String profession, Long after,
                                        Long before, Boolean banned, Integer minExperience,
                                        Integer maxExperience, Integer minLevel, Integer maxLevel);
    List<Player> findAllByParamsPagination(String name,
                                           String title, String race, String profession, Long after,
                                           Long before, Boolean banned, Integer minExperience,
                                           Integer maxExperience, Integer minLevel, Integer maxLevel,
                                           int pageNumber, int size, String order);
}
