[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player returns each creature card from his or her graveyard " +
                "to the battlefield with an additional -1/-1 counter on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(event) each {
                game.doAction(new ReanimateAction(
                    it,
                    it.getController(),
                    [MagicPlayMod.PERSIST]
                ));
            }
        }
    }
]
