[
    new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer drawPlayer) {
            return new MagicEvent(
                permanent,
                drawPlayer,
                this,
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
        }
    }
]
