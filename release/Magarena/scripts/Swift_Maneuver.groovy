[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 2 damage that would be dealt to target creature or player\$ this turn. "+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicPreventDamageAction(it,2));
                game.doAction(new MagicAddTriggerAction(
                    MagicAtUpkeepTrigger.YouDraw(
                        event.getSource(), 
                        event.getPlayer()
                    )
                ));
            });
        }
    }
]
