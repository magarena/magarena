[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two Swamps?"),
                this,
                "PN may\$ sacrifice two Swamps. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.getPlayer().getNrOfPermanents(MagicSubType.Swamp) >=2 && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),new MagicTargetChoice("a swamp to sacrifice")));
			game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),new MagicTargetChoice("a swamp to sacrifice")));
            } else {
                game.doAction(new MagicSacrificeAction(perm));
            }
        }
    }
]
