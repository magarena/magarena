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
            final int amount = game.filterCards(
                cardName("Kjeldoran War Cry")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
            ).size()+1;
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                CREATURE_YOU_CONTROL
            );
            for (final MagicPermanent creature : targets) {
                game.doAction(new ChangeTurnPTAction(creature,amount,amount));
            }
        }
    }
]
