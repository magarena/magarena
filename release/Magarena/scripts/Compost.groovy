[
    new MagicWhenOtherPutIntoGraveyardTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
            return act.getToLocation() == MagicLocationType.Graveyard;
        }
    
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicCard card = act.card;
            return (card.isEnemy(permanent) &&
                    act.to(MagicLocationType.Graveyard) &&
                    card.hasColor(MagicColor.Black)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ draw a card."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }
    }
]
