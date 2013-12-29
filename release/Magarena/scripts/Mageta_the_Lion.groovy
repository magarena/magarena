[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{W}{W}"), new MagicDiscardEvent(source, 2)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all other creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicTargetFilter<MagicPermanent> targetFilter =
                    new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE,permanent);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(permanent.getController(),targetFilter);
            for (final MagicPermanent target : targets) {
                game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
                }
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
