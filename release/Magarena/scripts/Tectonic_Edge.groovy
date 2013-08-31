[
    new MagicPermanentActivation(
        [
            //add ONE for the card itself
            MagicConditionFactory.ManaCost("{2}"),
            MagicCondition.OPP_FOUR_LANDS_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source),
                new MagicPayManaCostTapEvent(source,"{1}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_NONBASIC_LAND,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target nonbasic land\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                }
            });
        }
    }
]
