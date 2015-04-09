[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice three Forests?"),
                this,
                "PN may\$ sacrifice three Forests. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Forest) >=3) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_FOREST));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_FOREST));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_FOREST));
            } else {
                game.doAction(new MagicSacrificeAction(perm));
            }
        }
    }
]
