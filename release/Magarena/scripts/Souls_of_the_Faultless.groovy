[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer attackingPlayer = damage.getSource().getController();
            return (damage.getTarget() == permanent &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "PN gains RN life and " +
                    attackingPlayer + " loses RN life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer attackingPlayer = game.getTurnPlayer();
            game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefInt()));
            game.doAction(new ChangeLifeAction(attackingPlayer, -event.getRefInt()));
        }
    }
]
