package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.CustomExceptionBadRequest;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@RestController
//@RequestMapping("rest/players")
public class PlayerController {

    private final PlayerService service;


    public PlayerController(@Qualifier("playerServiceImpl") PlayerService service) {
        this.service = service;
    }

    //1.Get players list

    @RequestMapping(path = "/rest/players", method = RequestMethod.GET)
    public List<Player> findAll(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "race", required = false) Race race,
                                @RequestParam(value = "profession", required = false) Profession profession,
                                @RequestParam(value = "after", required = false) Long after,
                                @RequestParam(value = "before", required = false) Long before,
                                @RequestParam(value = "banned", required = false) Boolean banned,
                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                @RequestParam(value = "order", required = false) PlayerOrder order) {
        //add to tempList
        List<Player> playersIn = service.getPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);
        //sorted tempList
        List<Player> sortedPlayersIn = service.sortPlayers(playersIn, order);

        return service.getPage(sortedPlayersIn, pageNumber, pageSize);
//        return service.findAllByParamsPagination(
//                name.toLowerCase(),
//                title,
//                race,
//                profession,
//                after,
//                before,
//                banned,
//                minExperience,
//                maxExperience,
//                minLevel,
//                maxLevel, pageNumber, pageSize, order);
    }

    // 2.Get players count

    @RequestMapping(path ="rest/players/count", method = RequestMethod.GET)
    public Integer findAllByParams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
//        return service.findAllByParams(
//                name.toLowerCase(),
//                title,
//                race,
//                profession,
//                after,
//                before,
//                banned,
//                minExperience,
//                maxExperience,
//                minLevel,
//                maxLevel);
        return service.getPlayers(name, title, race, profession, after, before, banned,
                                  minExperience, maxExperience, minLevel, maxLevel).size();
    }

    //3.Create player

   @RequestMapping(path = "rest/players", method = RequestMethod.POST)
   @ResponseBody
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {

        if (!service.isValid(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (Objects.isNull(player.isBanned())) {
            player.setBanned(false);
        }

        return new ResponseEntity<>(service.savePlayer(player), HttpStatus.OK);
    }

 /*   @DeleteMapping("/{id}")
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
    }*/

}
