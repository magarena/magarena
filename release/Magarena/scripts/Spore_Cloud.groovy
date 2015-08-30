[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Tap all blocking creatures. Prevent all combat damage that would be dealt this turn. "+
                "Each attacking creature and each blocking creature doesn't untap during its controller's next untap step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            BLOCKING_CREATURE.filter(event) each {
                game.doAction(new TapAction(it));
            }
            game.doAction(new AddTurnTriggerAction(MagicPreventDamageTrigger.PreventCombatDamage));
            ATTACKING_OR_BLOCKING_CREATURE.filter(event) each {
                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.DoesNotUntapDuringNext));
            }
        }
    }
]
