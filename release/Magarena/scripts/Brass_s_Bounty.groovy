[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "For each land PN controls, " +
                "PN creates a colorless Treasure artifact token with \"{T}, Sacrifice this artifact: Add one mana of any color.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("colorless Treasure artifact token"),
                event.getPlayer().getNrOfPermanents(MagicType.Land)
            ));
        }
    }
]

