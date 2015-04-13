[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all combat damage that would be dealt this turn. PN gains 1 life for each attacking creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(
                MagicIfDamageWouldBeDealtTrigger.PreventCombatDamage
            ));
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getOpponent().getNrOfAttackers() + player.getNrOfAttackers();
            game.doAction(new MagicChangeLifeAction(player,amount));
        }
    }
]
