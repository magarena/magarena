// When SN dies, you draw X cards and you lose X life, where X is the number of creature cards in target player's graveyard.

[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "PN draws X cards and loses X life, where X is the number of creature cards in target player's graveyard"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                int creatureCards = it.getGraveyard().getNrOf(MagicType.Creature);
                game.doAction(new DrawAction(event.getPlayer(), creatureCards));
                game.doAction(new ChangeLifeAction(event.getPlayer(), -creatureCards));
            });
        }
    }
]
