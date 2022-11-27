package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Tournament;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {

}