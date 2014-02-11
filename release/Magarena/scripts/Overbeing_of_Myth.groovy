[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = player.getHandSize();
            pt.set(amount,amount);
        }
    },
	new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer == permanent.getController() ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN draws a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player));
        }
    }
]
