[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                this,
                "Target creature \$ gets +X/+0 until end of turn, where X is the number of creature cards in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.filterCards(event.getPermanent().getController(),MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size();
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(
                    it,
                    amount,
                    0
                    ));
            });
        }
    }
]
