package com.game.service.impl;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository repository;

    public PlayerServiceImpl(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Player> getPlayers(String name, String title, Race race, Profession profession,
                                   Long after, Long before, Boolean banned, Integer minExperience,
                                   Integer maxExperience, Integer minLevel, Integer maxLevel) {
        Date afterDate = Objects.isNull(after) ? null : new Date(after);
        Date beforeDate = before == null ? null : new Date(before);
        List<Player> tempList = new ArrayList<>();
        repository.findAll().forEach(player -> {
                    if (name != null && !player.getName().contains(name)) return;
                    if (title != null && !player.getTitle().contains(title)) return;
                    if (race != null && player.getRace() != race) return;
                    if (profession != null && player.getProfession() != profession) return;
                    if (afterDate != null && player.getBirthday().before(afterDate)) return;
                    if (beforeDate != null && player.getBirthday().after(beforeDate)) return;
                    if (banned != null && player.isBanned().booleanValue() != banned.booleanValue()) return;
                    if (minExperience != null && player.getExperience().compareTo(minExperience) < 0) return;
                    if (maxExperience != null && player.getExperience().compareTo(maxExperience) > 0) return;
                    if (minLevel != null && player.getLevel().compareTo(minLevel) < 0) return;
                    if (maxLevel != null && player.getLevel().compareTo(maxLevel) > 0) return;

                    tempList.add(player);
                }
        );

        return tempList;
    }

    @Override
    public List<Player> sortPlayers(List<Player> players, PlayerOrder order) {
        if (order != null) {
            players.sort((player1, player2) -> {
                switch (order) {
                    case ID:
                        return player1.getId().compareTo(player2.getId());
                    case NAME:
                        return player1.getName().compareTo(player2.getName());
                    case EXPERIENCE:
                        return player1.getExperience().compareTo(player2.getExperience());
                    case BIRTHDAY:
                        return player1.getBirthday().compareTo(player2.getBirthday());
                    case LEVEL:
                        return player1.getLevel().compareTo(player2.getLevel());
                    default:
                        return 0;
                }
            });
        }
        return players;
    }

    @Override
    public List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize) {

        int page = pageNumber == null ? 0 : pageNumber;
        int size = pageSize == null ? 3 : pageSize;
        int from = page * size;
        int to = from + size;
        if (to > players.size()) {
            to = players.size();
        }
        return players.subList(from, to);
    }

    @Override
    public Player savePlayer(Player player) {

        assignLevel(player);
        setExperienceToNextLevel(player);

        return repository.save(player);
    }

    @Override
    public boolean isValid(Player player) {

        return nameIsValid(player.getName()) && titleIsValid(player.getTitle()) &&
                raceIsValid(player.getRace()) && professionIsValid(player.getProfession()) &&
                birthdayIsValid(player.getBirthday()) && experienceIsValid(player.getExperience());
    }

    @Override
    public boolean nameIsValid(String name) {
        if (name != null) {
            return name.length() <= 12 && !name.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public boolean titleIsValid(String title) {
        if (title != null) {
            return title.length() <= 30 && !title.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public boolean raceIsValid(Race race) {
        return race != null;
    }

    @Override
    public boolean professionIsValid(Profession profession) {
        return profession != null;
    }

    @Override
    public boolean birthdayIsValid(Date birthday) {
        int year = birthday.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().getYear();
        return year >= 2000 && year <= 3000 && birthday.getTime() >= 0;
    }

    @Override
    public boolean experienceIsValid(Integer experience) {
        return experience >= 0 && experience <= 10000000;
    }

    private void assignLevel(Player player) {
        int experience = player.getExperience();

        int level = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;

        player.setLevel(level);
    }

    private void setExperienceToNextLevel(Player player) {
        int level = player.getLevel();

        int nextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();

        player.setUntilNextLevel(nextLevel);
    }

    @Override
    public Player findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Long serviceForId(String id) {
        if (id == null) {
            return null;
        } else
            try {
                return Long.parseLong(id);
            } catch (NumberFormatException e) {
                return null;
            }
    }

    @Override
    public Player updatePlayer(Player playerOut, Player playerIn) {

        //name
        if (nameIsValid(playerOut.getName())) {
            playerIn.setName(playerOut.getName());
        } else {
            if(playerOut.getName() != null) {
                throw new IllegalArgumentException();
            }
        }

        //title
        if (titleIsValid(playerOut.getTitle())) {
            playerIn.setTitle(playerOut.getTitle());
        } else {
            if (playerOut.getTitle() == null) {
            } else {
                throw new IllegalArgumentException();
            }
        }

        //race
        if (raceIsValid(playerOut.getRace())) {
            playerIn.setRace(playerOut.getRace());
        }

        //profession

        if (professionIsValid(playerOut.getProfession())) {
            playerIn.setProfession(playerOut.getProfession());
        }

        //birthday
        if (playerOut.getBirthday() != null) {
            if (birthdayIsValid(playerOut.getBirthday())) {
                playerIn.setBirthday(playerOut.getBirthday());
            } else {
                throw new IllegalArgumentException();
            }
        }

        //banned
        if (playerOut.isBanned() != null) {
            playerIn.setBanned(playerOut.isBanned());
        }

        //experience
        if (playerOut.getExperience() != null) {
            if (experienceIsValid(playerOut.getExperience())) {
                playerIn.setExperience(playerOut.getExperience());
            } else {
                throw new IllegalArgumentException();
            }
        }

        return savePlayer(playerIn);
    }

    @Override
    public void deletePlayerFromDB(Player player) {
        repository.delete(player);
    }

}
