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
            for (final MagicPlayer player : game.getAPNAP()) {
                final int lands = player.getNrOfPermanents(MagicType.Land);
                if (lands >= 6) {
                    final int amount = player.getNrOfPermanents(MagicType.Land) - 5;
                    game.addEvent(new MagicRepeatedCostEvent(
                        event.getSource(),
                        SACRIFICE_LAND,
                        amount,
                        MagicChainEventFactory.Sac
                    ));
                } else if (lands <= 4) {
                    final int amount = 5 - player.getNrOfPermanents(MagicType.Land);
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event.getSource(),
                        player,
                        new MagicFromCardFilterChoice(
                            BASIC_LAND_CARD_FROM_LIBRARY,
                            amount,
                            true,
                            "to put onto the battlefield"
                        )
                    ));
                }
            }
        }
    }
]
