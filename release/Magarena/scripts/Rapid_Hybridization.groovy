[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target creature.\$ It can't be regenerated. That creature's controller "+
                "puts a 3/3 green Frog Lizard creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
                game.doAction(new PlayTokenAction(
                    it.getController(),
                    CardDefinitions.getToken("3/3 green Frog Lizard creature token")
                ));
            });
        }
    }
]
