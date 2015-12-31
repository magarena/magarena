[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Target creature\$ PN controls gets +X/+0 until end of turn, where X is the number of creature cards in PN's graveyard. " +
                "Sacrifice that creature at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
                game.doAction(new ChangeTurnPTAction(it, X, 0));
                game.doAction(new AddTriggerAction(it, AtEndOfTurnTrigger.Sacrifice));
            });
        }
    }
]
