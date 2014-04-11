[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getController(),
                new MagicFromCardListChoice(cardOnStack.getController().getOpponent().getHand(),1),
                cardOnStack.getController().getOpponent(),
                this,
                "RN reveals his or her hand. PN chooses a card\$ from it. RN discards that card."+
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if(event.getCardChoice().size() > 0) {
                game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),event.getCardChoice().get(0)));
            }
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    }
]
