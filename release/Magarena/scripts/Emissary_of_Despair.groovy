[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "PN loses 1 life for each artifact he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer();
            final int amount = opponent.getNrOfPermanents(MagicType.Artifact);
            game.logAppendMessage(opponent, "("+amount+")");
            game.doAction(new ChangeLifeAction(opponent,-amount));
        }
    }
]
