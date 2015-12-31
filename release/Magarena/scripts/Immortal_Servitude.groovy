[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "PN returns each creature card with converted mana cost " + amount +
                " from his or her graveyard to the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            final MagicPlayer player = event.getPlayer();
            CREATURE_CARD_FROM_GRAVEYARD.filter(event) each {
                if (it.getConvertedCost() == amount) {
                    game.doAction(new ReanimateAction(
                        it,
                        player
                    ));
                }
            }
        }
    }
]
