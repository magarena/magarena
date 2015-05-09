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
            ENCHANTMENT_CARD_FROM_GRAVEYARD.filter(event) each {
                game.doAction(new ReanimateAction(it, event.getPlayer()));
            }
        }
    }
]
