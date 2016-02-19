[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return otherPermanent.isCreature() &&
                otherPermanent != permanent &&
                otherPermanent.isFriend(permanent) &&
                MagicColor.isColorless(otherPermanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent,
                    this,
                    "PN may\$ change SN's base power and toughness to RN's power and toughness until end of turn."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final int power = event.getRefPermanent().getPower();
                final int toughness = event.getRefPermanent().getToughness();
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
                        @Override
                        public void modPowerToughness(
                            final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                            pt.set(power, toughness);
                        }
                    }
                ));
            }
        }
    }
]
