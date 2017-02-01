/*
 * Stats schema script for a H2 database 
 * (http://www.h2database.com).
 *
*/

/*
**
** TABLES
**
*/

create table GAMESTATS_SETTINGS (
  SCHEMA_VERSION varchar(25) 
);

create table PLAYER (
  ID int identity, 
  AI_TYPE varchar(25), 
  AI_LEVEL int, 
  AI_XLIFE int, 
  PLAYER_PROFILE varchar(36)  -- Human-only player profile guid.
);

create table DECK_TYPE (
  ID int identity, 
  NAME varchar(15) not null 
);

create table DECK (
  ID int identity, 
  NAME varchar(100),    -- assumes a deck name will be 100 chars or less.
  FILE_CHECKSUM bigint, 
  DECK_TYPE_ID int, 
  DECK_SIZE int, 
  DECK_COLOR varchar(5)
);

create unique index IDX_PK_DECK 
  on DECK(NAME, FILE_CHECKSUM, DECK_TYPE_ID);

alter table DECK 
  add constraint DECK_TYPE_CONSTRAINT 
  foreign key (DECK_TYPE_ID) 
  references DECK_TYPE (ID);

create table GAME (
  TIME_START bigint primary KEY,    -- game start time as epoch
  MAG_VERSION varchar(7),           -- magarena version
  CONCEDED boolean,
  TURNS int,
  START_HAND int,
  START_LIFE int,
  WINNING_PLAYER_NUMBER int
);

create table GAME_PLAYER (
  GAME_TIME_START bigint NOT NULL,
  PLAYER_NUMBER int NOT NULL,    -- during a game, eg. player 1, player 2, etc.
  PLAYER_ID int, 
  DECK_ID int
);

alter table GAME_PLAYER 
  add primary key (GAME_TIME_START, PLAYER_NUMBER);

alter table GAME_PLAYER 
  add constraint GAME_CONSTRAINT 
  foreign key (GAME_TIME_START) 
  references GAME (TIME_START);

alter table GAME_PLAYER 
  add constraint PLAYER_CONSTRAINT 
  foreign key (PLAYER_ID) 
  references PLAYER (ID);

alter table GAME_PLAYER 
  add constraint DECK_CONSTRAINT 
  foreign key (DECK_ID) 
  references DECK (ID);


/*
**
** VIEWS
**
*/

create or replace view ALL_GAME_STATS as 
select
  G.TIME_START,
  G.MAG_VERSION,
  G.WINNING_PLAYER_NUMBER as WINNER,
  G.CONCEDED,
  G.TURNS,
  G.START_HAND,
  G.START_LIFE,
  PL1.PLAYER_PROFILE as P1_PROFILE,
  PL1.AI_TYPE as P1_AI_TYPE,
  PL1.AI_LEVEL as P1_AI_LEVEL,
  PL1.AI_XLIFE as P1_AI_XLIFE,
  D1.NAME as P1_DECK,
  D1.FILE_CHECKSUM as P1_DECK_CRC,
  DT1.NAME as P1_DECK_TYPE,
  D1.DECK_SIZE as P1_DECK_SIZE,
  D1.DECK_COLOR as P1_DECK_COLOR,
  PL2.PLAYER_PROFILE as P2_PROFILE,
  PL2.AI_TYPE as P2_AI_TYPE,
  PL2.AI_LEVEL as P2_AI_LEVEL,
  PL2.AI_XLIFE as P2_AI_XLIFE,
  D2.NAME as P2_DECK,
  D2.FILE_CHECKSUM as P2_DECK_CRC,
  DT2.NAME as P2_DECK_TYPE,
  D2.DECK_SIZE as P2_DECK_SIZE,
  D2.DECK_COLOR as P2_DECK_COLOR 
from
  GAME as G 
  inner join GAME_PLAYER as GP1 
    on G.TIME_START = GP1.GAME_TIME_START 
  inner join GAME_PLAYER as GP2 
    on G.TIME_START = GP2.GAME_TIME_START 
  inner join DECK as D1 
    on GP1.DECK_ID = D1.ID 
  inner join DECK as D2 
    on GP2.DECK_ID = D2.ID 
  inner join DECK_TYPE as DT1 
    on D1.DECK_TYPE_ID = DT1.ID 
  inner join DECK_TYPE as DT2 
    on D2.DECK_TYPE_ID = DT2.ID 
  inner join PLAYER as PL1 
    on GP1.PLAYER_ID = PL1.ID 
  inner join PLAYER as PL2 
    on GP2.PLAYER_ID = PL2.ID 
where
  GP1.PLAYER_NUMBER = 1 
  and GP2.PLAYER_NUMBER = 2;


create or replace view GAME_RESULTS as 
select
  GP.GAME_TIME_START as GAME,
  DK.NAME as DECK,
  DK.FILE_CHECKSUM as DECK_CRC,
  DT.NAME as DECK_TYPE,
  GM.WINNING_PLAYER_NUMBER as WINNER 
from
  GAME_PLAYER as GP 
  left join GAME as GM 
    on GP.GAME_TIME_START = GM.TIME_START 
    and GP.PLAYER_NUMBER = GM.WINNING_PLAYER_NUMBER 
  join DECK as DK 
    on GP.DECK_ID = DK.ID 
  join DECK_TYPE as DT 
    on DK.DECK_TYPE_ID = DT.ID;


create or replace view DECK_GAME_PWL as 
select
  DECK,
  DECK_CRC,
  DECK_TYPE,
  COUNT(GAME) as P,
  COUNT(WINNER) as W,
  SUM(NVL2(WINNER, 0, 1)) as L 
from
  GAME_RESULTS 
group by
  DECK, DECK_CRC, DECK_TYPE 
order by
  P desc, W desc, DECK;


create or replace view POPULAR_DECKS as
select 
  DECK, 
  DECK_CRC, 
  DECK_TYPE, 
  count(GAME) as P
from 
  GAME_RESULTS
where
  DECK_CRC > 0    -- ignore Random decks which have no associated deck file.
group by 
  DECK, DECK_CRC, DECK_TYPE
order by 
  P desc, DECK;


create or replace view WINNING_DECKS as 
select
  DECK,
  DECK_CRC,
  DECK_TYPE,
  count(WINNER) as W,
  count(GAME) as P 
from
  GAME_RESULTS 
where
  DECK_CRC > 0    -- ignore Random decks which have no associated deck file.
group by
  DECK, DECK_CRC, DECK_TYPE
having
  W > 0
order by
  W desc, P desc, DECK;


create or replace view RECENT_DECKS as 
select distinct
  DECK,
  DECK_CRC,
  DECK_TYPE,
  max(GAME) as last_played 
from
  GAME_RESULTS 
where
  DECK_CRC > 0    -- ignore Random decks which have no associated deck file.
group by
  DECK, DECK_CRC, DECK_TYPE 
order by
  last_played desc, DECK;


