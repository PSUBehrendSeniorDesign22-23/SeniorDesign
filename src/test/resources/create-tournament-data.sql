INSERT INTO RULESET(ruleset_id, name, origin) VALUES
(11, 'RulesetOne', 'North America'),
(12, 'RulesetTwo', 'Japan');

INSERT INTO USER_ACCOUNT(user_id, first_name, Last_name, email, phone_num) VALUES
(10, 'John', 'Doe', 'john.doe@example.com', '01112223333'),
(11, 'Jane', 'Doe', 'jane.doe@example.com', '02223334444'),
(12, 'Richard', 'Cook', 'richard.cook@example.com', '03334445555'),
(13, 'John', 'Mcmahon', 'john.mcmahon@example.com', '04445556666'),
(14, 'Stanford', 'Potter', 'stanford.potter@example.com', '05556667777');

INSERT INTO PLAYER(user_id, skipper_name, rank) VALUES 
(10, 'JohnnyBoy', 1000),
(11, 'JaneTime', 1000),
(12, 'RichyRich', 1000),
(13, 'JackTheRipper', 1000),
(14, 'SkipperMan', 1000);

INSERT INTO TOURNAMENT(name, location, date, ruleset_id) VALUES
('TournamentOne', 'North America', '1980-12-07', 11),
('TournamentTwo', 'Japan', '1980-12-08', 12);