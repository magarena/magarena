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
            final int amt = you.getNrOfPermanents(MagicSubType.Swamp);
            CREATURE.filter(event) each {
                game.doAction(new ChangeTurnPTAction(
                    it,
                    -amt,
                    -amt
                ));
            }
        }
    }
]
