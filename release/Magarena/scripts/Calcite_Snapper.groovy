[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ switch SN's power and toughness until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicAddStaticAction(event.getPermanent(), new MagicStatic(
                    MagicLayer.SwitchPT,
                    MagicStatic.UntilEOT) {
                    @Override
                    public void modPowerToughness(
                            final MagicPermanent source,
                            final MagicPermanent permanent,
                            final MagicPowerToughness pt) {
                        pt.set(pt.toughness(),pt.power());
                    }
                }));
            }
        }
    }
]
