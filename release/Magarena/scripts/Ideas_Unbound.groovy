def DISCARD_THREE_CARDS = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            game.addDelayedAction(new MagicRemoveTriggerAction(this));
            return new MagicEvent(
                game.createDelayedSource(staleSource, stalePlayer),
                this,
                "PN discards three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),3));
        }
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws three cards. PN discards three cards at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),3));
            game.doAction(new AddTriggerAction(DISCARD_THREE_CARDS(event.getSource(), event.getPlayer())));
        }
    }
]
