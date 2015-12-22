[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Shuffle all creature cards from target player's\$ graveyard into that player's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final List<MagicCard> graveyard = CREATURE_CARD_FROM_GRAVEYARD.filter(it);
                for (final MagicCard card : graveyard) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.TopOfOwnersLibrary
                    ));
                }
                game.doAction(new ShuffleLibraryAction(it));
            });
        }
    }
]
