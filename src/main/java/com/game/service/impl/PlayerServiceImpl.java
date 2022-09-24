package com.game.service.impl;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public List<Player> findAllByParams(String name, String title, String race, String profession, Long after,
                                        Long before, Boolean banned, Integer minExperience,
                                        Integer maxExperience, Integer minLevel, Integer maxLevel) {
        Date dateAfter = new Date(after);
        Date dateBefore = new Date(before);

        return repository.findAllByParams(
                name,
                title,
                Race.valueOf(race),
                Profession.valueOf(profession),
                dateAfter,
                dateBefore,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel);
    }

    @Override
    public List<Player> findAllByParamsPagination(String name, String title, String race, String profession,
                                                  Long after, Long before, Boolean banned, Integer minExperience,
                                                  Integer maxExperience, Integer minLevel, Integer maxLevel,
                                                  int pageNumber, int size, String order) {
        Date dateAfter = new Date(after);
        Date dateBefore = new Date(before);

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Order.by(order)));

        return repository.findAllByParamsPagination(name, title, Race.valueOf(race), Profession.valueOf(profession),
                dateAfter, dateBefore, banned, minExperience,
                maxExperience, minLevel, maxLevel, pageable);
    }
}
