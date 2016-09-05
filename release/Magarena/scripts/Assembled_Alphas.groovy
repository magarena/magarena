[
    new ThisBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent blocker) {
            final MagicPermanent target = permanent == blocker ? blocker.getBlockedCreature() : blocker;
            return new MagicEvent(
                permanent,
                target,
                this,
                "SN deals 3 damage to RN and RN's controller."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, 3));
                game.doAction(new DealDamageAction(event.getSource(), it.getController(), 3));
            });
        }
    }
]
