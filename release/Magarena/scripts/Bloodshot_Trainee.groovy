def POWER_4_OR_GREATER_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicPermanent permanent = (MagicPermanent)source;
        return permanent.getPower() >= 4;
    }
};

[
    new MagicPermanentActivation(
        [POWER_4_OR_GREATER_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,4));
            });
        }
    }
]
