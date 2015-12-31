[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all combat damage that would be dealt this turn by creatures with power 4 or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            new MagicPTTargetFilter(CREATURE,4).filter(event) each {
                game.doAction(new AddTurnTriggerAction(
                    it,
                    PreventDamageTrigger.PreventCombatDamageDealtBy
                ));
            }
        }
    }
]
