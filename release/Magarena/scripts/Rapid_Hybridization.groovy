[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target creature.\$ It can't be regenerated. That creature's controller "+
                "puts a 3/3 green Frog Lizard creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(MagicChangeStateAction.Set(permanent,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(permanent));
                game.doAction(new MagicPlayTokenAction(
                    permanent.getController(),
                    TokenCardDefinitions.get("3/3 green Frog Lizard creature token")
                ));
            });
        }
    }
]
