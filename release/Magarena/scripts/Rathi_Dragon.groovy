[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two Mountains?"),
                this,
                "PN may\$ sacrifice two Mountains. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Mountain) >=2) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_MOUNTAIN));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_MOUNTAIN));
            } else {
                game.doAction(new MagicSacrificeAction(perm));
            }
        }
    }
]
