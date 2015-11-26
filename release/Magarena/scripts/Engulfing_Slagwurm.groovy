[
    new ThisBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                permanent == blocker ? blocker.getBlockedCreature() : blocker,
                this,
                "Destroy RN. PN gains life equal to its toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent target = event.getRefPermanent();
            game.doAction(new DestroyAction(target));
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                target.getToughness()
            ));
        }
    }
]
