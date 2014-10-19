[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                this,
                "Target creature \$ PN controls gets +X/+0 until end of turn, where X is the number of creature cards in PN's graveyard. " +
                "Sacrifice that creature at the beginning of the next end step."
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
                game.doAction(new MagicAddTriggerAction(it, MagicAtEndOfTurnTrigger.Sacrifice));            });
        }
    }
]
