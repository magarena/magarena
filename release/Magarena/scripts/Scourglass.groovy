def NONLAND_NONARTIFACT_PERMANENT = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.isLand() && !target.isArtifact();
    }
};

[
    new MagicPermanentActivation(
        [
            MagicCondition.YOUR_UPKEEP_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all permanents except for artifacts and lands."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(NONLAND_NONARTIFACT_PERMANENT.filter(event)));
        }
    }
]
