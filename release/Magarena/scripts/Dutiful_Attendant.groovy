[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final MagicTargetChoice TARGET_OTHER_CREATURE_CARD_FROM_GRAVEYARD=new MagicTargetChoice(
                CREATURE_CARD_FROM_GRAVEYARD.except(died.getCard()),
                MagicTargetHint.None,
                "a creature other than " + died + " to return to hand"
            );
            return new MagicEvent(
                permanent,
                TARGET_OTHER_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "PN returns another target creature card from his or her graveyard\$ to his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ShiftCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersHand
                ));
            });
        }
    }
]
