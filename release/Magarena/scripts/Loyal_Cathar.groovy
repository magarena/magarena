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
                    "PN returns RN to the battlefield transformed."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReanimateAction(event.getRefCard(),event.getPlayer(),MagicPlayMod.TRANSFORMED));
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
                "Return SN to the battlefield transformed under PN's control at the beginning of the next end step."
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


