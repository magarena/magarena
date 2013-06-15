[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_GOBLIN_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,
            final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }

        @Override
        public boolean condition(final MagicGame game,
            final MagicPermanent source, final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump, false), 
        "Regen"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_GOBLIN_CREATURE, source);
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter, true, MagicTargetHint.Positive,
                "another target Goblin");
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
