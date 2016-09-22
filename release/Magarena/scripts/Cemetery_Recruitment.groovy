[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "PN returns target creature card\$ from his or her graveyard to his or her hand. " +
                "If it's a Zombie card, PN draws a card."
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
                if (it.hasSubType(MagicSubType.Zombie)) {
                    game.doAction(new DrawAction(event.getPlayer()));
                    game.logAppendMessage(event.getPlayer(), "(Yes)");
                }
            });
        }
    }
]
