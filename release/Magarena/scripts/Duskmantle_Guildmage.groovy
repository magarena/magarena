def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicPlayer owner = act.card.getOwner();
            return owner.getId() != stalePlayer.getId() ?
                new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    owner,
                    this,
                    "PN loses 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -1));
        }
    };
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{U}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Whenever a card is put into an opponent's graveyard from anywhere this turn, that player loses 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(DelayedTrigger(event.getSource(), event.getPlayer())));
        }
    }
]
