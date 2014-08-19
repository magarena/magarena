[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a mountain?"),
                this,
                "PN may\$ sacrifice a mountain. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),MagicTargetChoice.SACRIFICE_MOUNTAIN);
            if (event.isYes() && sac.hasOptions(game)) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
