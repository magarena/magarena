def choice = new MagicTargetChoice("another creature to sacrifice");

[
    new AtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices another creature. If PN can't, SN deals 6 damage to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sacrifice = new MagicSacrificePermanentEvent(event.getSource(), choice);
            if (sacrifice.isSatisfied()) {
                game.addEvent(sacrifice);
            } else {
                game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), 6));
            }
        }
    }
]
