package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {


    @Query("SELECT pl FROM Player pl " +
            "WHERE pl.name LIKE CONCAT('%', :name1, '%')" +
            "AND pl.title LIKE CONCAT('%', :title1, '%')" +
            "AND pl.race = :race " +
            "AND pl.profession = :prof " +
            "AND (pl.birthday BETWEEN :after AND :before)" +
            " AND pl.banned = :banned " +
            "AND (pl.experience BETWEEN :minExp AND :maxExp)" +
            "AND (pl.level BETWEEN :minL AND :maxL)")
            List<Player> findAllByParamsPagination(@Param("name1") String name,
                                @Param("title1") String title,
                                @Param("race") Race race,
                                @Param("prof") Profession profession,
                                @Param("after") Date after,
                                @Param("before") Date before,
                                @Param("banned") Boolean banned,
                                @Param("minExp") Integer minExperience,
                                @Param("maxExp") Integer maxExperience,
                                @Param("minL") Integer minLevel,
                                @Param("maxL") Integer maxLevel, Pageable pageable);

    @Query("SELECT pl FROM Player pl " +
            "WHERE pl.name LIKE CONCAT('%', :name1, '%')" +
            "AND pl.title LIKE CONCAT('%', :title1, '%')" +
            "AND pl.race = :race " +
            "AND pl.profession = :prof " +
            "AND (pl.birthday BETWEEN :after AND :before)" +
            " AND pl.banned = :banned " +
            "AND (pl.experience BETWEEN :minExp AND :maxExp)" +
            "AND (pl.level BETWEEN :minL AND :maxL)")


            List<Player> findAllByParams(@Param("name1") String name,
                                @Param("title1") String title,
                                @Param("race") Race race,
                                @Param("prof") Profession profession,
                                @Param("after") Date after,
                                @Param("before") Date before,
                                @Param("banned") Boolean banned,
                                @Param("minExp") Integer minExperience,
                                @Param("maxExp") Integer maxExperience,
                                @Param("minL") Integer minLevel,
                                @Param("maxL") Integer maxLevel);
       /* List<Player> findAllByNameContainingAndTitleContainingAndRaceAndProfessionAndBirthdayBetweenAndBannedAndExperienceBetweenAndLevelBetweenAndPageNumberAndPageSizeAndOrder(
                String name,
                String title,
                Race race,
                Profession profession,
                Date after,
                Date before,
                Boolean banned,
                Integer minExperience,
                Integer maxExperience,
                Integer minLevel,
                Integer maxLevel,
                int pageNumber,
                int pageSize,
                String order);*/
}

