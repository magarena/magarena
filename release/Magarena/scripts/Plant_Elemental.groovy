[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a Forest?"),
                this,
                "PN may\$ sacrifice a Forest. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.getPlayer().controlsPermanent(MagicSubType.Forest) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_FOREST));
            } else {
                game.doAction(new SacrificeAction(perm));
            }
        }
    }
]
