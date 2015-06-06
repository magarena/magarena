[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player who controls six or more lands chooses five lands he or she controls and sacrifices the rest. " +
                "Each player who controls four or fewer lands may search his or her library for up to X basic land cards " +
                "and put them onto the battlefield, where X is five minus the number of lands he or she controls. " +
                "Then each player who searched his or her library this way shuffles it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            if (player.getNrOfPermanents(MagicType.Land) >= 6) {    
                final int n1 = player.getNrOfPermanents(MagicType.Land) - 5;
                n1.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,SACRIFICE_LAND));
                }
            } else if (player.getNrOfPermanents(MagicType.Land) <= 4) {
                final int n2 = 5 - player.getNrOfPermanents(MagicType.Land);
                n2.times {
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event.getSource(),
                        player,
                        new MagicMayChoice(
                            "Search for basic land card?",
                            A_BASIC_LAND_CARD_FROM_LIBRARY
                        )
                    ));
                }
            };
            if (opponent.getNrOfPermanents(MagicType.Land) >= 6) {    
                final int n3 = opponent.getNrOfPermanents(MagicType.Land) - 5;
                n3.times {
                    game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),opponent,SACRIFICE_LAND));
                }
            } else if (opponent.getNrOfPermanents(MagicType.Land) <= 4) {
                final int n4 = 5 - opponent.getNrOfPermanents(MagicType.Land);
                n4.times {
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event.getSource(),
                        opponent,
                        new MagicMayChoice(
                            "Search for basic land card?",
                            A_BASIC_LAND_CARD_FROM_LIBRARY
                        )
                    ));
                }
            };
        }
    }
]
