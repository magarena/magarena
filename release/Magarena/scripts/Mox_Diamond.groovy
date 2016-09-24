[
    new MagicETBEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(),
                payedCost,
                this,
                "PN may\$ discard a land card. " + 
                "If you do, put SN onto the battlefield. " + 
                "If you don't, put it into its owner's graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicDiscardChosenEvent(event.getSource(), MagicTargetChoice.LAND_CARD_FROM_HAND);
            final MagicCardOnStack spell = event.getCardOnStack();
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
                spell.setMoveLocation(MagicLocationType.Battlefield);
                game.addEvent(MagicPlayCardEvent.create().getEvent(spell, event.getRefPayedCost()));
            } else {
                game.logAppendMessage(event.getPlayer(), "Put ${spell} into its owner's graveyard.");
            }
        }
    }
]
