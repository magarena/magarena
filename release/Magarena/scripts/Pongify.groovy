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
                "That creature's controller puts a 3/3 green Ape " +
                "creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final MagicPlayer controller = creature.getController();
                game.doAction(MagicChangeStateAction.Set(
                    creature,
                    MagicPermanentState.CannotBeRegenerated
                ));
                game.doAction(new MagicDestroyAction(creature));
                game.doAction(new MagicPlayTokenAction(
                    controller,
                    TokenCardDefinitions.get("3/3 green Ape creature token")
                ));
            });
        }
    }
]
