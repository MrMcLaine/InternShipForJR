package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("rest/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(@Qualifier("playerServiceImpl") PlayerService service) {
        this.service = service;
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player, @RequestParam(value = "id", defaultValue = "0") Long id) {
        int year = 0;
        try {
            year = player.getBirthday().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate().getYear();
        } catch (NullPointerException exception) {
            throw new IllegalArgumentException();
        }

        if (player.getName().length() > 12 ||
                player.getName().isEmpty() ||
                player.getTitle().length() > 30 ||
                player.getTitle().isEmpty() ||
                player.getExperience() > 10_000_000 ||
                player.getExperience() < 0 ||
                player.getBirthday().getTime() < 0 ||
                year < 2000 || year > 3000 ||
                Objects.isNull(player.getRace()) ||
                Objects.isNull(player.getProfession()) ||
                player.isBanned()) {
            throw new IllegalArgumentException();
        }
        player.setId(id);

        //указаны не все параметры из Data Params (кроме banned);
//        - длина значения параметра “name” или “title” превышает размер соответствующего поля в БД (12 и 30 символов);
//        - значение параметра “name” пустая строка;
//        - опыт находится вне заданных пределов;
//        - “birthday”:[Long] < 0;
//        - дата регистрации находятся вне заданных пределов.
//                В случае всего вышеперечисленного необходимо ответить ошибкой с кодом 400.


        service.save(player);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Player findById(@PathVariable("id") Long id) {
        Player player = service.findById(id);

        if (Objects.isNull(player)) {
            throw new NullPointerException();
        }

        return player;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Player> findAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                @RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
                                @RequestParam(value = "order", defaultValue = "id") String order,
                                @RequestParam(value = "name", defaultValue = "name") String name,
                                @RequestParam(value = "title", defaultValue = "title") String title,
                                @RequestParam(value = "race", defaultValue = "HUMAN") String race,
                                @RequestParam(value = "profession", defaultValue = "WARRIOR") String profession,
                                @RequestParam(value = "after", defaultValue = "0") Long after,
                                @RequestParam(value = "before", defaultValue = "0") Long before,
                                @RequestParam(value = "banned", defaultValue = "false") Boolean banned,
                                @RequestParam(value = "minExperience", defaultValue = "0") Integer minExperience,
                                @RequestParam(value = "maxExperience", defaultValue = "0") Integer maxExperience,
                                @RequestParam(value = "minLevel", defaultValue = "0") Integer minLevel,
                                @RequestParam(value = "maxLevel", defaultValue = "0") Integer maxLevel) {
        return service.findAllByParamsPagination(
                name.toLowerCase(),
                title,
                race,
                profession,
                after,
                before,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel, pageNumber, pageSize, order.toLowerCase());
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public List<Player> findAllByParams(
            @RequestParam(value = "name", defaultValue = "name") String name,
            @RequestParam(value = "title", defaultValue = "title") String title,
            @RequestParam(value = "race", defaultValue = "HUMAN") String race,
            @RequestParam(value = "profession", defaultValue = "WARRIOR") String profession,
            @RequestParam(value = "after", defaultValue = "0") Long after,
            @RequestParam(value = "before", defaultValue = "0") Long before,
            @RequestParam(value = "banned", defaultValue = "false") Boolean banned,
            @RequestParam(value = "minExperience", defaultValue = "0") Integer minExperience,
            @RequestParam(value = "maxExperience", defaultValue = "0") Integer maxExperience,
            @RequestParam(value = "minLevel", defaultValue = "0") Integer minLevel,
            @RequestParam(value = "maxLevel", defaultValue = "0") Integer maxLevel) {
        return service.findAllByParams(
                name.toLowerCase(),
                title,
                race,
                profession,
                after,
                before,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel);
    }

}
