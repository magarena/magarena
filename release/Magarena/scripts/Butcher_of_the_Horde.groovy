[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Choice"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    source
                ),
                MagicTargetHint.None,
                "a creature other than " + source + " to sacrifice"
            );
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    targetChoice
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ - vigilance(1); lifelink(2); haste(3)"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Vigilance));
            } else if (event.isMode(2)){
                game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Lifelink));
            } else if (event.isMode(3)){
                game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Haste));
            }
        }
    }
]
