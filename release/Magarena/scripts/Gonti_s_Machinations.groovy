[
    new LifeIsLostTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
            return (permanent.isController(data.player) && permanent.getController().getLifeLossThisTurn() == 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gets {E}."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPlayer(), MagicCounterType.Energy, 1));
        }
    }
    ,
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Lose life"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayEnergyEvent(source, 2),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getController(),
                source.getOpponent(),
                this,
                "RN loses 3 life. PN gains life equal to the life lost this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final ChangeLifeAction action = new ChangeLifeAction(event.getRefPlayer(), -3);
            game.doAction(action);
            if (action.getLifeChange() < 0) {
                game.doAction(new ChangeLifeAction(event.getPlayer(), -action.getLifeChange()));
            }
        }
    }
]

