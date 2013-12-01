[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target creature\$. It can't be regenerated. " +
                "Its controller puts a 1/1 white Spirit creature " +
                "token with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final MagicPlayer controller=creature.getController();
                game.doAction(MagicChangeStateAction.Set(
                    creature,
                    MagicPermanentState.CannotBeRegenerated
                ));
                game.doAction(new MagicDestroyAction(creature));
                game.doAction(new MagicPlayTokenAction(
                    controller,
                    TokenCardDefinitions.get("1/1 white Spirit creature token with flying")
                ));
            });
        }
    }
]
