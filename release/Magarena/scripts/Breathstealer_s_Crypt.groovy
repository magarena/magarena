def action = {
    final MagicGame game, final MagicEvent event ->
        final MagicCard card = event.getRefCard();
        final MagicPlayer player = card.getOwner();
        final MagicEvent costEvent = new MagicPayLifeEvent(event.getSource(), player, 3);
        if (event.isYes() && costEvent.isSatisfied()) {
            game.addEvent(costEvent);
        } else if (card.isInHand()) {
            game.logAppendMessage(player, player.getName() + " discards " + card.getName());
            game.doAction(new DiscardCardAction(player, card));
        }
}
[
    new OtherDrawnTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            new MagicEvent(
                permanent,
                card.getController(),
                card,
                this,
                "PN draws a card and reveals it. If it's a creature card, "+
                "that player discards it unless he or she pays 3 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getRefCard();
            game.doAction(new RevealAction(card));
            if (card.hasType(MagicType.Creature)) {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    card.getController(),
                    new MagicMayChoice(),
                    card,
                    action,
                    "PN may pay 3 life.\$ If not, discard " + card
                ));
            }
        }
    }
]
