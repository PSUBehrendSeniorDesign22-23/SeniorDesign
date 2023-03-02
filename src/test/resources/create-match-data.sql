INSERT INTO RULESET(ruleset_id, name, origin) VALUES
(11, 'RulesetOne', 'North America'),
(12, 'RulesetTwo', 'Japan');

INSERT INTO USER_ACCOUNT(user_id, first_name, last_name, email, phone_num) VALUES
(10, 'John', 'Doe', 'john.doe@example.com', '01112223333'),
(11, 'Jane', 'Doe', 'jane.doe@example.com', '02223334444'),
(12, 'Richard', 'Cook', 'richard.cook@example.com', '03334445555'),
(13, 'John', 'Mcmahon', 'john.mcmahon@example.com', '04445556666'),
(14, 'Stanford', 'Potter', 'stanford.potter@example.com', '05556667777');

INSERT INTO PLAYER(player_id, user_id, skipper_name, rank) VALUES 
(10, 10, 'JohnnyBoy', 1000),
(11, 11, 'JaneTime', 1000),
(12, 12, 'RichyRich', 1000),
(13, 13, 'JackTheRipper', 1000),
(14, 14, 'SkipperMan', 1000);

INSERT INTO TOURNAMENT(tournament_id, name, location, date, ruleset_id) VALUES
(11, 'TournamentOne', 'North America', '1980-12-07', 11),
(12, 'TournamentTwo', 'Japan', '1980-12-08', 12);

INSERT INTO MATCH(player_one_id, player_two_id, player_one_score, player_two_score, tournament_id) VALUES
(11, 12, 1, 2, 11),
(12, 11, 2, 1, 12),
(13, 14, 2, 1, 11),
(14, 11, 1, 2, 12);