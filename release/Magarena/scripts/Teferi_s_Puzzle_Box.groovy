[
    new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer drawPlayer) {
            return new MagicEvent(
                permanent,
                drawPlayer,
                this,
                "PN puts the cards in his or her hand on the bottom of his or her library in any order, "+
                "then draws that many cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList hand = new MagicCardList(event.getPlayer().getHand());
            for (final MagicCard card : hand) {
                game.addEvent(new MagicTuckCardEvent(event.getPermanent(),event.getPlayer()));
            }
            game.addEvent(new MagicDrawEvent(event.getPermanent(),event.getPlayer(),hand.size()));
        }
    }
]
