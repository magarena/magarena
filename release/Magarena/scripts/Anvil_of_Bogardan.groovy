[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            player.noMaxHandSize();
        }
    },
    new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer drawPlayer) {
            return new MagicEvent(
                permanent,
                drawPlayer,
                this,
                "PN draws a card, then discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
            game.addEvent(new MagicDiscardEvent(event.getSource(),player));
        }
    }
]
