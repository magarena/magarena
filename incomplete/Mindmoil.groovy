[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
            return (permanent.isFriend(spell)) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts the cards in his or her hand on the bottom of his or her library in any order, "+
                    "then draws that many cards."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList hand = new MagicCardList(player.getHand());
            for (final MagicCard card : hand) {
                game.addEvent(new MagicTuckCardEvent(event.getPermanent(), event.getPlayer(),false));
            }
            game.logAppendMessage(player,"PN puts "+hand.size()+" cards on the bottom of his or her library.");
            game.addEvent(new MagicDrawEvent(event.getPermanent(), event.getPlayer(), hand.size()));
        }
    }
]
