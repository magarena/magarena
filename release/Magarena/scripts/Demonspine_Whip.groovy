[
    // equipped creature gets +X/+0 until end of turn
    new MagicPermanentActivation(
        [MagicCondition.HAS_EQUIPPED_CREATURE],
        new MagicActivationHints(MagicTiming.Pump),
        "+X/+0"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "Equipped creature ("+source.getEquippedCreature()+") gets +X/+0 until end of turn (X="+payedCost.getX()+")."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent().getEquippedCreature(),event.getRefInt(),0));
        }
    }
]
