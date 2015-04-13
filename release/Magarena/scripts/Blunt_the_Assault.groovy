[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 1 life for each creature on the battlefield. " +
                "Prevent all combat damage that would be dealt this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = game.getNrOfPermanents(MagicType.Creature);
            game.doAction(new ChangeLifeAction(player,amount));
            game.doAction(new AddTurnTriggerAction(
                MagicIfDamageWouldBeDealtTrigger.PreventCombatDamage
            ));
        }
    }
]
