[
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            card.reveal();
            return card.hasType(MagicType.Creature) ? 
                new MagicEvent(
                    card,
                    new MagicMayChoice("Pay 3 life? If not, discard " + card),
                    this,
                    "PN draws SN. PN may\$ pay 3 life. If PN doesn't, discard SN."
                ): new MagicEvent(
                    card,
                    MagicEvent.NO_ACTION,
                    "PN draws SN."
                );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), -3));
            } else {
                game.doAction(new MagicDiscardCardAction(event.getPlayer(), event.getCard()));
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
