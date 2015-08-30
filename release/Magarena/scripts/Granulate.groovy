[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy each nonland artifact with converted mana cost 4 or less."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent permanent : ARTIFACT.filter(event)) {
                if (permanent.isLand() == false && permanent.getConvertedCost() <= 4) {
                    game.doAction(new DestroyAction(permanent));
                }
            }
        }
    }
]
