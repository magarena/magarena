[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_PERMANENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target permanent\$. Its controller puts a 3/3 green Beast creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                game.doAction(new MagicPlayTokenAction(
                    permanent.getController(),
                    TokenCardDefinitions.get("3/3 green Beast creature token")
                ));
            });
        }
    }
]
