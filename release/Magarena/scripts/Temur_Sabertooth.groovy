[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Indestructible"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN may\$ return another creature you control to its owner's hand. If you do, SN gains indestructible until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                new MagicBounceChosenPermanentEvent(
                    event.getSource(), 
                    event.getPlayer(),
                    MagicTargetChoice.PosOther("target creature you control", event.getPermanent())
                );
                game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Indestructible));
            }
        }
    }
]
