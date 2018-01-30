def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer, final MagicCard staleCard ->
    return new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));

            final MagicCard mappedCard = staleCard.getOwner().map(game).getGraveyard().getCard(staleCard.getId());

            return mappedCard.isInGraveyard() ?
                new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    mappedCard,
                    this,
                    "Return RN to PN's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
        }
    }
}

[
    new OtherDiesTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
            return died.getCounters(MagicCounterType.MinusOne) > 0;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer()));
        }
    },
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

