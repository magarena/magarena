[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a Forest?"),
                this,
                "PN may\$ sacrifice a Forest. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Forest) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),MagicTargetChoice.SACRIFICE_FOREST));
            } else {
                game.doAction(new MagicSacrificeAction(permanent));
            }
        }
    }
]
