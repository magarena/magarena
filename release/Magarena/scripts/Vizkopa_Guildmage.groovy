def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new LifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            return stalePlayer.getId() == lifeChange.player.getId() ?
                new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    lifeChange.player.getOpponent(),
                    lifeChange.amount,
                    this,
                    "PN loses RN life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                -event.getRefInt()
            ));
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
                new MagicPayManaCostEvent(source,"{1}{W}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Whenever PN gains life this turn, each opponent loses that much life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(DelayedTrigger(event.getSource(), event.getPlayer())));
        }
    }
]
