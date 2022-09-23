package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    void addPlayer(Player player);

    List<Player> findAll(int pageNumber, int size, String order);
}
