[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a land?"),
                this,
                "PN may\$ sacrifice a land. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicType.Land) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),SACRIFICE_LAND));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
