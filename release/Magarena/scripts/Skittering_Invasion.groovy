[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts five 0/1 colorless Eldrazi Spawn creature tokens onto the battlefield." +
				"They have \"Sacrifice this creature: Add {1} to your mana pool.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Eldrazi Spawn"),
                5
            ));
        }
    }
]
