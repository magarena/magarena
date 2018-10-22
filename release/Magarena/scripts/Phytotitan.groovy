def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer, final MagicCard staleCard ->
    return new AtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return stalePlayer.getId() == upkeepPlayer.getId();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));
            final MagicCard mappedCard = staleCard.getOwner().map(game).getGraveyard().getCard(staleCard.getId());

            return mappedCard.isInGraveyard() ?
                new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    mappedCard,
                    this,
                    "PN returns RN to the battlefield tapped."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReanimateAction(event.getRefCard(),event.getPlayer(),MagicPlayMod.TAPPED));
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
                "Return SN to the battlefield tapped under its owner's control at the beginning of PN's next upkeep."
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
