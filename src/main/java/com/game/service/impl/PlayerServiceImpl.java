package com.game.service.impl;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository repository;

    public PlayerServiceImpl(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addPlayer(Player player) {
        repository.save(player);

    }

    @Override
    public List<Player> findAll(int pageNumber, int size, String order) {
        PageRequest pageRequest = PageRequest.of(pageNumber, size,Sort.by(Sort.Order.by(order)));
        return repository.findAll(pageRequest).toList();

    }
}
