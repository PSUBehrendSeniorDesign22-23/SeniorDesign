package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Tournament;

import java.util.List;
import java.sql.Date;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {

    Tournament findTournamentByName(String name);

    List<Tournament> findTournamentsByLocation(String location);

    List<Tournament> findTournamentsByDate(Date date);

    List<Tournament> findTournamentsByRulesetId(long rulesetId);

}