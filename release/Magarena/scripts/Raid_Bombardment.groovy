[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.isFriend(creature) && creature.getPower() <= 2) ?
                new MagicEvent(
                    permanent,
                    game.getDefendingPlayer(),
                    this,
                    "SN deals 1 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(
                event.getSource(),
                event.getPlayer(),
                1
            ));
        }
    }
]
