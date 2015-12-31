[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Target creature\$ can't be regenerated this turn. " +
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(ChangeStateAction.Set(
                    it,
                    MagicPermanentState.CannotBeRegenerated
                ));
                game.doAction(new AddTriggerAction(
                    AtUpkeepTrigger.YouDraw(
                        event.getSource(),
                        event.getPlayer()
                    )
                ));
            });
        }
    }
]
