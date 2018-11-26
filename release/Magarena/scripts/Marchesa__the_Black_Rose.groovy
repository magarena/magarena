def DelayedTrigger = {
    final MagicPermanent staleSource, final MagicPlayer stalePlayer, final MagicCard staleCard ->
    return new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));

            final MagicCard mappedCard = staleCard.getOwner().map(game).getGraveyard().getCard(staleCard.getId());

            return mappedCard.isInGraveyard() ?
                new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    mappedCard,
                    this,
                    "Return RN to the battlefield under PN's control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReanimateAction(
                event.getRefCard(),
                event.getPlayer()
            ));
        }
    };
}

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isCreature() && otherPermanent.isNonToken() && otherPermanent.isFriend(permanent) && otherPermanent.getCounters(MagicCounterType.PlusOne) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getCard(),
                    this,
                    "Return RN to the battlefield under PN's control at the beginning of the next end step."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(DelayedTrigger(
                event.getPermanent(),
                event.getPlayer(),
                event.getRefCard()
            )));
        }
    }
]
