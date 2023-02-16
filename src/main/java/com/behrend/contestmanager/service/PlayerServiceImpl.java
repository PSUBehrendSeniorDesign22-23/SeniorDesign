package com.behrend.contestmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.behrend.contestmanager.repository.PlayerRepository;;

@Service
public class PlayerServiceImpl implements PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;
}
