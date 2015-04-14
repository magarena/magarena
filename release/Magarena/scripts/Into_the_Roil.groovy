[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_NONLAND_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target nonland permanent\$ to its owner's hand. If SN was kicked, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                if (event.isKicked()) {
                    game.doAction(new DrawAction(event.getPlayer()));
                }
            });
        }
    }
]
