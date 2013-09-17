[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_GOBLIN_PERMANENT, 
                    source
                ),
                MagicTargetHint.Positive,
                "another target Goblin"
            );
            return new MagicEvent(
                source,
                targetChoice,
                MagicRegenerateTargetPicker.getInstance(),
                this,
                "Regenerate another target Goblin\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,
                new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    }
]
