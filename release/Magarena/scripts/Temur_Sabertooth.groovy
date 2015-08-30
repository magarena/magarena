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
                new MagicMayChoice(),
                this,
                "PN may\$ return another creature you control to its owner's hand. If you do, SN gains indestructible until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent bounce = new MagicBounceChosenPermanentEvent(
                event.getSource(), 
                event.getPlayer(),
                Other("a creature you control", event.getPermanent())
            );
            if (event.isYes() && bounce.isSatisfied()) {
                game.addEvent(bounce);
                game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Indestructible));
            }
        }
    }
]
