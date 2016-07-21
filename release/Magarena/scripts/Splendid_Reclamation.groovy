[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN returns all land cards from his or her graveyard to the battlefield tapped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            LAND_CARD_FROM_YOUR_GRAVEYARD.filter(event) each {
                game.doAction(new ReanimateAction(it, event.getPlayer(), MagicPlayMod.TAPPED));
            }
        }
    }
]
