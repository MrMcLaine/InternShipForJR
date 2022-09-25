package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.Date;
import java.util.List;

public interface PlayerService {

    List<Player> getPlayers(String name,String title, Race race, Profession profession, Long after,
                            Long before, Boolean banned, Integer minExperience,
                            Integer maxExperience, Integer minLevel, Integer maxLevel);
    List<Player> sortPlayers(List<Player> players, PlayerOrder order);

    List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize);

    Player savePlayer(Player player);

    boolean isValid(Player player);

    boolean nameIsValid(String name);
    boolean titleIsValid(String title);
    boolean raceIsValid(Race race);
    boolean professionIsValid(Profession profession);
    boolean birthdayIsValid(Date birthday);
    boolean experienceIsValid(Integer experience);

    Player findById(long id);

    Long serviceForId(String id);

    Player updatePlayer(Player playerIn, Player playerOut);


    void deletePlayerFromDB(Player player);

}
