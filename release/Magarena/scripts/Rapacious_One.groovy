[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getDealtAmount(),
                this,
                "PN creates RN 0/1 colorless Eldrazi Spawn creature tokens. " +
                "They have \"Sacrifice this creature: Add {C}.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("0/1 colorless Eldrazi Spawn creature token"),
                event.getRefInt()
            ));
        }
    }
]
