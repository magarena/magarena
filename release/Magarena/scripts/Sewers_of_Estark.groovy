[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Choose target creature.\$ If it's attacking, it can't be blocked this turn. "+
                "If it's blocking, prevent all combat damage that would be dealt this combat by it and each creature it's blocking."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (it.isAttacking()) {
                    game.doAction(new GainAbilityAction(it, MagicAbility.Unblockable, MagicStatic.UntilEOT));
                } else if (it.isBlocking()) {
                    game.doAction(new AddTurnTriggerAction(it, PreventDamageTrigger.PreventCombatDamageDealtBy));
                    game.doAction(new AddTurnTriggerAction(it.getBlockedCreature(), PreventDamageTrigger.PreventCombatDamageDealtBy));
                }
            });
        }
    }
]
