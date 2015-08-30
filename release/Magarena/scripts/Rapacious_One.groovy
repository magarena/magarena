[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getDealtAmount(),
                this,
                "Put RN 0/1 colorless Eldrazi Spawn creature tokens onto the battlefield. " + 
                "They have \"Sacrifice this creature: Add {1} to your mana pool.\""
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
