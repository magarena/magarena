def choice = new MagicTargetChoice("target nonblack creature");

def CREATURE_WITH_BOUNTY_COUNTER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasCounters(MagicCounterType.Tower) && target.isCreature();
    }
};

def TARGET_CREATURE_WITH_BOUNTY_COUNTER = new MagicTargetChoice(
    CREATURE_WITH_BOUNTY_COUNTER,
    "target creature with a bounty counter on it"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Counter"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                this,
                "Put a bounty counter on target nonblack creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                 game.doAction(new ChangeCountersAction(it,MagicCounterType.Tower,1));
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Destroy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_WITH_BOUNTY_COUNTER,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature with a bounty counter on it\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                 game.doAction(new DestroyAction(it));
            });
        }
    }
]
