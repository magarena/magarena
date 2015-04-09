[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice four creatures?"),
                this,
                "PN may\$ sacrifice four creatures. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicType.Creature) >=4) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_CREATURE));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_CREATURE));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_CREATURE));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),SACRIFICE_CREATURE));
            } else {
                game.doAction(new MagicSacrificeAction(perm));
            }
        }
    }
]
