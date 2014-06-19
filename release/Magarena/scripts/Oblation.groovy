[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_NONLAND_PERMANENT,
                MagicExileTargetPicker.create(),
                this,
                "The owner of target nonland permanent shuffles it into his or her library, then draws two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersLibrary));
                game.doAction(new MagicDrawAction(permanent.getController(),2));
            });
        }
    }
]
