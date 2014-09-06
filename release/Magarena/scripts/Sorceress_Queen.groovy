def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(0,2);
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
		final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE,
                    source
                ),
                MagicTargetHint.None,
                "another target creature"
            );          
            return new MagicEvent(
                source,
                targetChoice,
                new MagicBecomeTargetPicker(0,2,false),
                this,
                "Target creature\$ becomes 0/2 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicBecomesCreatureAction(it,PT));
            });
        }
    }
]
