[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 1 life for each creature his or her opponents control. "+
                "Prevent all damage that would be dealt to PN this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = CREATURE_YOU_CONTROL.filter(player.getOpponent()).size();
            game.logAppendValue(player,amount)
            game.doAction(new ChangeLifeAction(player, amount));
            game.doAction(new AddTurnTriggerAction(
                PreventDamageTrigger.PreventDamageDealtToYou(player)
            ));
        }
    }
]
