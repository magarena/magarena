[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Prevent all combat damage that would be dealt by creatures other than target creature\$ this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame Game, final MagicEvent event) {
            event.processTargetPermanent(Game, {
                CREATURE.except(it).filter(it) each {
                    Game.doAction(new AddTurnTriggerAction(it, PreventDamageTrigger.PreventCombatDamageDealtBy));
                }
            });
        }
    }
]
