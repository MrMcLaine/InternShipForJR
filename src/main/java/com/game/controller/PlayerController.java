package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(@Qualifier("playerServiceImpl") PlayerService service) {
        this.service = service;
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player) {
        service.addPlayer(player);
    }

    @GetMapping
    public List<Player> findAll(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("order") String  order) {
        return service.findAll(pageNumber, pageSize, order.toLowerCase());
    }

}
