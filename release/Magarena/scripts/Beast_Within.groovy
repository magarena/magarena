[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PERMANENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target permanent\$. Its controller puts a 3/3 green Beast creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.doAction(new PlayTokenAction(
                    it.getController(),
                    CardDefinitions.getToken("3/3 green Beast creature token")
                ));
            });
        }
    }
]
