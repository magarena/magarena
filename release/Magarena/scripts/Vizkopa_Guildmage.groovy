def DelayedTrigger = {
    final MagicSource source, final MagicPlayer player ->
    return new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            return player.getId() == lifeChange.player.getId() ?
                new MagicEvent(
                    game.createDelayedSource(source, player),
                    lifeChange.player.getOpponent(),
                    lifeChange.amount,
                    this,
                    "PN loses RN life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(
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
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
            game.doAction(new MagicAddTurnTriggerAction(DelayedTrigger(event.getSource(), event.getPlayer())));
        }
    }
]
