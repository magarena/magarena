[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Target creature \$ gets +X/+0 until end of turn, where X is the number of creature cards in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = event.getPlayer().filterCards(CREATURE_CARD_FROM_GRAVEYARD).size();
                game.doAction(new MagicChangeTurnPTAction(it, X, 0));
            });
        }
    }
]
