[
    new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent == died && permanent.getOwner() == permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(MagicSimpleMayChoice.DRAW_CARDS, 0, MagicSimpleMayChoice.DEFAULT_NONE),
                    this,
                    "PN may\$ return SN to his/her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicCard card = event.getSource().getCard()
            if (MagicMayChoice.isYesChoice(choiceResults[0]) && event.getPlayer().getGraveyard().contains(card)) {
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
