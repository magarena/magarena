[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each attacking creature gets +1/+0 until end of turn for each nonbasic land defending player controls.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = NONBASIC_LAND_YOU_CONTROL.filter(game.getDefendingPlayer()).size();
            game.logAppendMessage(event.getPlayer(), "("+amount+")");
            ATTACKING_CREATURE.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, amount,0));
            }
        }
    }
]
