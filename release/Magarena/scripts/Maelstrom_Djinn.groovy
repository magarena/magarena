[
    new MagicWhenTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                this,
                "Put two time counters on SN and it gains vanishing."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Time,2));
            game.doAction(new AddTriggerAction(event.getPermanent(), MagicFadeVanishCounterTrigger.Time));
        }
    }
]
