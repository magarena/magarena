[
    new OtherDrawnTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return card.hasType(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Pay 3 life? If not, discard " + card),
                    card,
                    this,
                    "PN may\$ pay 3 life. If PN doesn't, discard RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicPayLifeEvent(event.getSource(), 3);
            final MagicCard card = event.getRefCard();
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else if (card.isInHand()) {
                game.doAction(new DiscardCardAction(event.getPlayer(), card));
            }
        }
    }
]
