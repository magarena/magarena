[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                permanent == blocker ? blocker.getBlockedCreature() : blocker,
                this,
                "SN deals 3 damage to RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getPermanent(),
                event.getRefPermanent(),
                3
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
