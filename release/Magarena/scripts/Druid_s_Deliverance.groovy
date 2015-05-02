[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all combat damage that would be dealt to you this turn. Populate."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(
                MagicPreventDamageTrigger.PreventCombatDamageDealtToYou(event.getPlayer())
            ));
            game.addEvent(new MagicPopulateEvent(event.getSource()));
        }
    }
]
