[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN returns all enchantment cards from his or her graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> targets = game.filterCards(
                    event.getPlayer(),
                    ENCHANTMENT_CARD_FROM_GRAVEYARD);
            for (final MagicCard card : targets) {
                game.doAction(new MagicReanimateAction(card,event.getPlayer()));
            }
        }
    }
]
