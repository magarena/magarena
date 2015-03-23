[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Protection"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PERMANENT_YOU_CONTROL,
                this,
                "Creatures you control gain protection from the colors of target permanent\$ you control until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicColor color : MagicColor.values()) {
                if (it.hasColor(color)) {
                    final Collection<MagicPermanent> creatures = event.getPlayer().filterPermanents(MagicTargetFilterFactory.CREATURE);
                    for (final MagicPermanent creature : creatures) {
                        game.doAction(new MagicGainAbilityAction(creature,color.getProtectionAbility()));
                        }
                    }
                }
            });
        }
    }
]
