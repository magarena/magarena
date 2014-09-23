[
    new MagicSpellCardEvent() {

        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                this,
                "Prevent all combat damage that would be dealt to and dealt by target creature\$ this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddTurnTriggerAction(
                    it,
                    MagicIfDamageWouldBeDealtTrigger.PreventCombatDamageDealtToDealtBy
                ));
            });
        }
    }
]
