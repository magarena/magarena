[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = source.getEnchantedPermanent().getController().getHandSize();
            pt.add(amt, amt);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    new AtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer == permanent.getEnchantedPermanent().getController() ?
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
            game.doAction(new DrawAction(player));
        }
    }
]
