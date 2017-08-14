[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature.\$ power. "+
                "Its controller manifests the top card of his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
                game.doAction(new ManifestAction(it.getController(), 1));
            });
        }
    }
]
