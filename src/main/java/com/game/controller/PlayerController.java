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

    @PostMapping("/{id}")
    public void addPlayer(@RequestBody Player player, @PathVariable("id") Long id) {
        player.setId(id);
        service.save(player);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id){
        service.deleteById(id);
    }

    @GetMapping("/{id}")
    public Player findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }


    @RequestMapping(method = RequestMethod.GET )
    public List<Player> findAll(@RequestParam("pageNumber") int pageNumber,
                                @RequestParam("pageSize") int pageSize,
                                @RequestParam("order") String  order) {
        return service.findAll(pageNumber, pageSize, order.toLowerCase());
    }

    @RequestMapping(value ="/count",method = RequestMethod.GET )
    public List<Player> findAll1(@RequestParam("pageNumber") int pageNumber,
                                @RequestParam("pageSize") int pageSize,
                                @RequestParam("order") String  order) {
        return service.findAll(pageNumber, pageSize, order.toLowerCase());
    }

//    @RequestMapping(method = RequestMethod.GET )
//    public List<Player> findAll1() {
//        return service.findAll1();
//    }

}
