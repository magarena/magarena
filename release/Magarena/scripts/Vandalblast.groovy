[
    new MagicCardActivation(
        [
            MagicConditionFactory.ManaCost("{4}{R}")
        ],
        new MagicActivationHints(MagicTiming.Tapping,true),
        "Overload"
    ) {
        public MagicEvent[] getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, source.getController(), MagicManaCost.create("{4}{R}"))
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy each artifact you don't control."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets= 
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS);
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
