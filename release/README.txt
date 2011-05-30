Magarena 
========
Homepage: http://magarena.googlecode.com
Forum: http://www.slightlymagic.net/forum/viewforum.php?f=82

Requirements: Java Runtime 6 (http://java.com) must be installed on your computer

Starting Magarena:
  On Windows, double click on Magarena.exe
  On Linux, execute ./Magarena.sh. 
  On Mac, run Magarena.command

Magarena supports the following keyboard shortcuts :
  Space or Right key : action button
  Escape or Left key : undo button
  Enter key : switch between graphical and text mode
  F1 or M key : show or hide game messages

Selecting the AI to play against:
The desired AI can be selected in the preferences (Arena -> Preferences).
Default is MiniMax. AI difficulty level for the best balance of speed and
strength is the default 6.  The Vegas AI should be fast on all difficulty
levels. The MCTS AI is considered experimental but it is also the strongest.

Thanks to
  ubeefx for creating such a great game
  epiko for creating the very nice Magarena logo and the amazing color themes
  Salasnet for the felt theme and pedro1973 for the dark battle theme
  singularita for creating the scripts to add over 300 additional creature cards
  Melvin Zhang for implementing the Monte Carlo Tree Search AI
  LSK and Kuno for contributing custom decks
  pedro1973 for creating new themes  (http://www.slightlymagic.net/forum/viewforum.php?f=89)
  mtgrares for the publicity
  everyone on the CCGHQ forum (http://slightlymagic.net/forum/)

Thank you for your support and have fun!

Release 1.14 (May 28, 2011)
============
- 601 cards in default cube
- 901 cards in all cube (includes those in default)

- added Naya Hushblade (Alara Reborn, Common)

- restored ability to download card images from the WWW
- fixed bug with protection from monocolored and land that become creatures
- fixed bug with "as long as you control another multicolored permanent" and
  land that become creatures
- enhanced error handling by printing error messages to stderr when an exception occurs
- renamed "can't be the target of spells or abilities your opponents control"
  to hexproof
- by default all card images shown at 312x445 for consistency, can be changed
  via option in preference menu to show original image size
- filter legal targets off by default as it is confusing to new users
- no need to restart after downloading images
- added hexproof to list of keywords

Release LE 1.13 (April 24, 2011)
===============

- default cube (600 cards)
- all cube (900 cards)

- added sound effects, disabled by default, can be enabled in preferences
- right mouse click on hand zone can now be used as a shortcut for action button
- infect and wither abilities are also shown on cards with same icon as deathtouch

Release LE 1.12 (April 17, 2011)
===============

- default cube (567 cards)
- all cube (888 cards)

- modified game to work with a premade Magarena data folder
- added selectable avatar sets in preferences, separate from theme
- added unlimited undo support 
- added "Reset game" in menu, undoing all moves
- added M key as an additional shortcut for messages

Release 1.11 (April 11, 2011)
============

- default cube (555 cards)
- all cube (876 cards)

- renamed the two standard cubes to default and all
- improved displaying of messages with scroll bar and toggle button + F1 shortcut to show or hide messages
- improved mana cost images (if already installed, delete symbols folder in Magarena data folder and load images)
- improved card definition files, they now also contain the image url and cube information
- improved scoring for unnecessary equiping
- three new AI implementations are available next to the default MiniMax AI : Monte Carlo, Vegas and Random
- the preferred AI can be selected in preferences, the deck strength calculator always uses MiniMax AI
- added New and Load Duel buttons on startup screen
- added option to select a random deck from decks folder in New Duel dialog (folder icon)

- fixed Lightning Helix, it can now target your own permanents with filter legal targets enabled

Release 1.10 (April 1, 2011)
============

- ubeefx cube (544 cards)
- singularita cube (865 cards)

- redesigned card image loading (low quality images are no longer needed)
- high quality random card at startup if enabled in settings

Release 1.9 (March 22, 2011)
===========

- ubeefx cube (542 cards)
- singularita cube (862 cards)

- if a folder Magarena is present in the same folder as where Magarena is started, that is used as the Magarena data folder
- added extra settings to themes (see customizing Wiki page)
- added support for infect keyword and poison counters

- fixed Solemn Offering, it can now target your own permanents with filter legal targets enabled
- fixed handling of invalid avatars

Release 1.8 (March 13, 2011)
===========

- ubeefx cube (530 cards)
- singularita cube (850 cards)

- improved deck generator
- added option to play with monocolored generated decks
- changed function of Life + slider in difficulty settings so that it gives extra life to computer for added difficulty

- fixed must attack if able when power is zero or lower
- fixed apostrophes in Keywords screen

Release 1.7 (January 14, 2011)
===========

- 8 new cards (512 total)

- new splash screen and small UI tweaks
- added mana info for lands in hand and on battlefield
- added Enter as hotkey to switch between image and text mode
- added support for user made UI themes that can be downloaded separately
- added support for user defined card cubes next to the default cube

- fixed Pongify, it can now target your own permanents with filter legal targets enabled

Release 1.6 (January 2, 2011)
===========

- 4 new cards (504 total)

- mana cost, power & toughness and some abilities on cards in hand are now displayed in graphical view
- card drawing is now optional for Snake Umbra and Mask of Memory
- added support for intimidate and battle cry keywords

- fixed how deathtouch was handled causing a number of issues
- fixed endless loop in custom card shuffler for decks with a lot of CMC > 4 cards
- fixed Into the Roil, it can now target your own permanents with filter legal targets enabled

Release 1.5 (December 28, 2010)
===========

- 5 new cards (500 total)

- added random card to the startup screen

Release 1.4 (December 26, 2010)
===========

- 5 new cards (495 total)

- added light wood skin
- improved display of valid choices
- improved AI destroy and exile target pickers

- fixed Omnibian ability on level up creatures

Release 1.3 (December 24, 2010)
===========
 
- 15 new cards (490 total)
- replaced Elite Vanguard with Steppe Lynx
- replaced Curse of Chains with Narcolepsy

- added Magarena icon to application frame
- added keyboard shortcuts for action button : space or right key
- added keyboard shortcuts for undo button : escape or left key
- increased maximum difficulty level to 8 (default is 6)
- configurable number of games and difficulty level for deck strength calculator
- configurable card image popup delay in preferences (0 - 500 ms)
- configurable high quality transparent card image popups (turned off by default)
- improved display of stack in graphical view
- zoom effect on photo gadget is now delayed
- added support for sub folders in images data folder

- fixed triggered ability of Archon of Justice when discarding

Release 1.2 (December 18, 2010)
===========

- 25 new cards (475 total)
- replaced Disperse with Into the Roil 
- replaced Gruul War Plow with Chimeric Mass

- support for loading and saving decks to an editable text format in decks folder
- disabled deck editing when duel is in progress
- added two and three color wild cards to new duel dialog
- charge counters are now displayed with amount in graphical view
- added tooltips for small icon buttons

- fixed aura or equipment that stays attached when creature has protection
- fixed sacrifice or exile at and end of turn not always triggering (Rakdos Guildmage)
- fixed self targeting of spells on stack (counters)
- fixed wrong multiple target behavior (Char and Goblin Artillery)
- fixed converted mana cost for X spells on stack (Draining Whelk)
- fixed triggered ability for multiple activations of Raging Ravine

Release 1.1 (December 7, 2010)
===========

- new splash screen
- display of version information on startup
- improved download images dialog with proxy support
- support for two color decks
- support for replacing cards in decks with edit button that shows the card explorer
- added Unhinged basic lands as alternate images for basic lands
- icons for summoning sickness, flying, first & double strike, trample and deathtouch in graphical view
- viewers for exile zone

- fixed summoning sickness rule
- fixed Spider Umbra aura

Release 1.0 (November 25, 2010)
===========

- initial release (450 cards)
