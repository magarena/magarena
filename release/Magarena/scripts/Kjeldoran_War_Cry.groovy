[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures PN controls get +X/+X until end of turn, " +
                "where X is 1 plus the number of cards named Kjeldoran War Cry in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Kjeldoran War Cry")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size()+1;
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            }
        }
    }
]
