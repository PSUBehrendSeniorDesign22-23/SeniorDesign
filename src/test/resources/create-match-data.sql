INSERT INTO RULESET(ruleset_id, name, origin) VALUES
(11, 'RulesetOne', 'North America'),
(12, 'RulesetTwo', 'Japan');

INSERT INTO PLAYER(player_id, first_name, Last_name, skipper_name, rank, email, phone_num) VALUES 
(11, 'John', 'Doe', 'JohnnyBoy', 1000, 'john.doe@example.com', '01112223333'),
(12, 'Jane', 'Doe', 'JaneTime', 1000, 'jane.doe@example.com', '02223334444'),
(13, 'Richard', 'Cook', 'RichyRich', 1000, 'richard.cook@example.com', '03334445555'),
(14, 'John', 'Mcmahon', 'JackTheRipper', 1000, 'john.mcmahon@example.com', '04445556666'),
(15, 'Stanford', 'Potter', 'SkipperMan', 1000, 'stanford.potter@example.com', '05556667777');

INSERT INTO TOURNAMENT(tournament_id, name, location, date, ruleset_id) VALUES
(11, 'TournamentOne', 'North America', '1980-12-07', 11),
(12, 'TournamentTwo', 'Japan', '1980-12-08', 12);

INSERT INTO MATCH(player_one_id, player_two_id, player_one_score, player_two_score, tournament_id) VALUES
(11, 12, 1, 2, 11),
(12, 11, 2, 1, 12),
(13, 14, 2, 1, 11),
(14, 15, 1, 2, 12);