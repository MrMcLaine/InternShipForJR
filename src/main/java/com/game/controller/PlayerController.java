package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
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

        service.save(player);
    }

    @PutMapping
    public void updatePlayer(@RequestBody Player player) {

        service.save(player);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id){
        service.deleteById(id);
    }

//    @GetMapping
//    public Player findById(@Param("id") Long id) {
//        return service.findById(id);
//    }


    @GetMapping
    public List<Player> findAll(@RequestParam("pageNumber") int pageNumber,
                                @RequestParam("pageSize") int pageSize,
                                @RequestParam("order") String  order) {
        return service.findAll(pageNumber, pageSize, order.toLowerCase());
    }

}
