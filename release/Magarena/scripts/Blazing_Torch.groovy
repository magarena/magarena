def EFFECT = MagicRuleEventAction.create("SN deals 2 damage to target creature or player.")

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent permanent) {
            return [
                new MagicTapEvent(permanent.getEquippedCreature()),
                new MagicSacrificeEvent(permanent)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return EFFECT.getEvent(permanent);
        }
    }
]
