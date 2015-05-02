[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all damage that would be dealt to you and creatures you control this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(
                MagicPreventDamageTrigger.PreventDamageDealtToYouOrCreaturesYouControl(event.getPlayer())
            ));
        }
    }
]
