[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
                final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                    source
                ),
                MagicTargetHint.None,
                "a creature other than " + source + " to sacrifice"
            );
            return [
                new MagicSacrificePermanentEvent(source,targetChoice),
                new MagicRegenerationConditionsEvent(source,this)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Regenerate SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRegenerateAction(event.getPermanent()));
        }
    }
]
