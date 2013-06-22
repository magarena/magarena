[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                permanent == blocker ? blocker.getBlockedCreature() : blocker,
                this,
                "SN deals 1 damage to RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getRefPermanent(),
                1
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
