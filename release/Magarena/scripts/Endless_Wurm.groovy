[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice an enchantment?"),
                this,
                "PN may\$ sacrifice an enchantment. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),SACRIFICE_ENCHANTMENT); 
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
