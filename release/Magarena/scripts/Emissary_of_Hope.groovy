[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "${permanent.getController().getName()} gains 1 life for each artifact PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer();
            final int amount = opponent.getNrOfPermanents(MagicType.Artifact);
            game.logAppendValue(opponent, amount);
            game.doAction(new ChangeLifeAction(event.getSource().getController(),amount));
        }
    }
]
