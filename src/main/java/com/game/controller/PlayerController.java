package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
//@RequestMapping("rest/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
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
    }

    // 2.Get players count

    @RequestMapping(path = "rest/players/count", method = RequestMethod.GET)
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

    //4. Get player
    @RequestMapping(path = "rest/players/{id}", method = RequestMethod.GET)
    public ResponseEntity<Player> findById(@PathVariable(value = "id") String pathId) {
        Long id = service.serviceForId(pathId);

        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player = service.findById(id);
        if (Objects.isNull(player)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }


    // 5. Update player
    @RequestMapping(path = "rest/players/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") String pathId,
                                               @RequestBody Player player) {
        ResponseEntity<Player> entity = findById(pathId);
        Player tempPlayer = entity.getBody();

        if (tempPlayer == null) {
            return entity;
        }

        Player newPlayer;
        try {
            newPlayer = service.updatePlayer(player, tempPlayer);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    //6. Delete player

    @RequestMapping(path = "rest/players/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Player> deletePlayer(@PathVariable(value = "id") String pathId) {
        ResponseEntity<Player> entity = findById(pathId);
        Player tempPlayer = entity.getBody();
        if (tempPlayer == null) {
            return entity;
        }
        service.deletePlayerFromDB(tempPlayer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
