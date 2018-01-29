def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer, final MagicCard refCard ->
    return new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));
            return new MagicEvent(
                game.createDelayedSource(staleSource, stalePlayer),
                refCard,
                this,
                "Return RN to PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
        }
    }
}

[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
            return new MagicEvent(
                source,
                source.getOwner(),
                source.getCard(),
                this,
                "Return SN to PN's hand at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(
                DelayedTrigger(event.getSource(), event.getPlayer(), event.getRefCard())
            ));
        }
    }
]

