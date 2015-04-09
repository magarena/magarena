[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get -1/-1 until end of turn for each Swamp you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer you = event.getPlayer();
            Collection<MagicPermanent> creatures = game.filterPermanents(
                you,
                CREATURE
            )
            final int amt = you.getNrOfPermanents(MagicSubType.Swamp);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(
                    creature,
                    -amt,
                    -amt
                ));
            }
        }
    }
]
