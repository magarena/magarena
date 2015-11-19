def choice = new MagicTargetChoice("a Swamp to sacrifice"); 

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two Swamps?"),
                this,
                "PN may\$ sacrifice two Swamps. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Swamp) >= 2) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(), choice));
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(), choice));
            } else {
                game.doAction(new SacrificeAction(perm));
            }
        }
    }
]
