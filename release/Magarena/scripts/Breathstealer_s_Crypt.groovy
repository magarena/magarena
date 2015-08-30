[
    new MagicWhenOtherDrawnTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return card.hasType(MagicType.Creature) ? 
                new MagicEvent(
                    card,
                    new MagicMayChoice("Pay 3 life? If not, discard " + card),
                    this,
                    "PN may\$ pay 3 life. If PN doesn't, discard this card."
                ) : 
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(), -3));
            } else {
                game.doAction(new DiscardCardAction(event.getPlayer(), event.getCard()));
            }
        }
    }
]
