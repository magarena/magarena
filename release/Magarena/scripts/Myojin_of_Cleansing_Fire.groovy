[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            if (permanent.hasState(MagicPermanentState.CastFromHand)) {
                game.doAction(new ChangeCountersAction(permanent, MagicCounterType.Divinity, 1));
            } 
            return MagicEvent.NONE;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Divinity,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all other creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets= game.filterPermanents(
                event.getPermanent().getController(),
                CREATURE.except(event.getPermanent())
            );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
