[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.None),
        "Return"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}{B}"),
                    new MagicSacrificeEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetChoice TARGET_OTHER_CREATURE_CARD_FROM_GRAVEYARD=new MagicTargetChoice(
                CREATURE_CARD_FROM_GRAVEYARD.except(source.getCard()),
                MagicTargetHint.None,
                "a creature other than " + source + " to return to hand"
            );
            return new MagicEvent(
                source,
                TARGET_OTHER_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "PN returns another target creature card\$ from his or her graveyard to his or her hand."
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
