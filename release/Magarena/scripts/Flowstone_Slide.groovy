[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get +"+amount+"/-"+amount+" until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer you = event.getPlayer();
            Collection<MagicPermanent> creatures = game.filterPermanents(
                you,
                CREATURE
            )
            final int amt = event.getCardOnStack().getX();
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(
                    creature,
                    amt,
                    -amt
                ));
            }
        }
    }
]
