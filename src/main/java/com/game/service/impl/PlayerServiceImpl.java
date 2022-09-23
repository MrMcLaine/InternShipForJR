package com.game.service.impl;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public void save(Player player) {

        assignLevel(player);
        setExperienceToNextLevel(player);

        repository.save(player);

    }

    private void assignLevel(Player player) {
        int experience = player.getExperience();

        int level = (int)(Math.sqrt(2500 + 200 * experience) - 50) / 100;

        player.setLevel(level);
    }

    private void setExperienceToNextLevel(Player player) {
        int level = player.getLevel();

        int nextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();

        player.setUntilNextLevel(nextLevel);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Player findById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Player> findAll(int pageNumber, int size, String order) {
        PageRequest pageRequest = PageRequest.of(pageNumber, size,Sort.by(Sort.Order.by(order)));
        return repository.findAll(pageRequest).toList();

    }

    @Override
    public List<Player> findAll1() {
        return repository.findAll();

    }
}
