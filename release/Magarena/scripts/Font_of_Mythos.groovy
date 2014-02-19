[
    new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN draws two cards."
                );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,2));
        }
    }
]
