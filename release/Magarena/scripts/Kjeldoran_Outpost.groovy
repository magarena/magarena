def costEffect = new MagicRegularCostEvent("Sacrifice a Plains");

[
    new MagicETBEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice("Sacrifice a Plains?"),
                payedCost,
                this,
                "PN may\$ sacrifice a Plains. " +
                "If you do, put SN onto the battlefield. " +
                "If you don't, put it into its owner's graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = costEffect.getEvent(event.getSource());
            final MagicCardOnStack spell = event.getCardOnStack();
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
                spell.setMoveLocation(MagicLocationType.Battlefield);
                game.addEvent(MagicPlayCardEvent.create().getEvent(spell, event.getRefPayedCost()));
            } else {
                game.logAppendMessage(event.getPlayer(), "Put ${spell} into its owner's graveyard.");
                game.doAction(new MoveCardAction(
                    event.getCardOnStack().getCard(),
                    MagicLocationType.OwnersHand,
                    MagicLocationType.Graveyard
                ));
            }
        }
    }
]
