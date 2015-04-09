def ProtectionFromArtifacts = MagicAbility.getAbilityList("protection from artifacts");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Color"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRepeatedPermanentsEvent(
                    source,
                    SACRIFICE_LAND,
                    2,
                    MagicChainEventFactory.Sac
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "SN gains protection from the color\$ of your choice until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(
                event.getPermanent(),
                event.getChosenColor().getProtectionAbility()
            ));
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Artifacts"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRepeatedPermanentsEvent(
                    source,
                    SACRIFICE_LAND,
                    2,
                    MagicChainEventFactory.Sac
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gains protection from artifacts until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(event.getPermanent(),ProtectionFromArtifacts))
        }
    }
]
