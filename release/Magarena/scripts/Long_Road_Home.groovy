def DelayedTrigger = {
    final MagicCard staleCard, final MagicPlayer stalePlayer ->
    return new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));

            final MagicCard mappedCard = staleCard.getOwner().map(game).getExile().getCard(staleCard.getId());

            return mappedCard.isInExile() ?
                new MagicEvent(
                    mappedCard,
                    this,
                    "Return SN to the battlefield under PN's control with a +1/+1 counter on it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PutOntoBattlefieldAction(
                MagicLocationType.Exile,
                event.getCard(),
                event.getPlayer(),
                [MagicPlayMod.UNDYING]
            ));
        }
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. " +
                "Return it to the battlefield under its owner's control at the beginning of the next end step with a +1/+1 counter on it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new AddTriggerAction(DelayedTrigger(it.getCard(),event.getPlayer())));
            });
        }
    }
]
