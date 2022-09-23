package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    void save(Player player);

    void deleteById(long id);

    Player findById(long id);


    List<Player> findAll(int pageNumber, int size, String order);
}
