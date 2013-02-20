[
    new MagicCardActivation(
        [
            MagicManaCost.ONE_BLUE_BLUE_RED.getCondition()
        ],
        new MagicActivationHints(MagicTiming.Tapping,true),
        "Overload"
    ) {
        public MagicEvent[] getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, source.getController(), MagicManaCost.ONE_BLUE_BLUE_RED)
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Counter each spell you don't control."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicItemOnStack> targets=
                game.filterItemOnStack(event.getPlayer(),MagicTargetFilter.TARGET_SPELL_YOU_DONT_CONTROL);
            for (final MagicItemOnStack targetSpell : targets) {
                game.doAction(new MagicCounterItemOnStackAction(targetSpell));
            }
        }
    }
]
