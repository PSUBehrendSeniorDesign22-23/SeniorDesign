package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Tournament;

import java.util.List;
import java.sql.Date;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {

    List<Tournament> findTournamentsByName(String name);

    List<Tournament> findTournamentsByLocation(String location);

    List<Tournament> findTournamentsByDate(Date date);

    List<Tournament> findTournamentsByRulesetId(long rulesetId);

}