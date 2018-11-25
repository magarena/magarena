def trigger = {
    final MagicCard staleCard ->
    return new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));

            final MagicCard mappedCard = staleCard.getOwner().map(game).getExile().getCard(staleCard.getId());

            return mappedCard.isInExile() ?
                new MagicEvent(
                    mappedCard,
                    mappedCard.getOwner(),
                    this,
                    "Return SN to the battlefield under PN's control tapped."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PutOntoBattlefieldAction(
                MagicLocationType.Exile,
                event.getCard(),
                event.getPlayer(),
                MagicPlayMod.TAPPED
            ));
        }
    };
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicDiscardEvent(source, 3),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile SN. Return it to the battlefield under its owner's control tapped at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
            game.doAction(new AddTriggerAction(trigger(event.getPermanent().getCard())));
        }
    }
]

