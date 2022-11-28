INSERT INTO RULESET(ruleset_id, name, origin) VALUES
(11, 'RulesetOne', 'North America'),
(12, 'RulesetTwo', 'Japan');

INSERT INTO PLAYER(first_name, Last_name, skipper_name, rank, email, phone_num) VALUES 
('John', 'Doe', 'JohnnyBoy', 1000, 'john.doe@example.com', '01112223333'),
('Jane', 'Doe', 'JaneTime', 1000, 'jane.doe@example.com', '02223334444'),
('Richard', 'Cook', 'RichyRich', 1000, 'richard.cook@example.com', '03334445555'),
('John', 'Mcmahon', 'JackTheRipper', 1000, 'john.mcmahon@example.com', '04445556666'),
('Stanford', 'Potter', 'SkipperMan', 1000, 'stanford.potter@example.com', '05556667777');

INSERT INTO TOURNAMENT(name, location, date, ruleset_id) VALUES
('TournamentOne', 'North America', '1980-12-07', 11),
('TournamentTwo', 'Japan', '1980-12-08', 12);