[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_NONLAND_PERMANENT,
                MagicExileTargetPicker.create(),
                this,
                "The owner of target nonland permanent shuffles it into his or her library, then draws two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersLibrary));
                game.doAction(new DrawAction(it.getController(),2));
            });
        }
    }
]
