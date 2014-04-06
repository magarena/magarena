[
    new MagicWhenBecomesMonstrousTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicChangeStateAction action) {
            return action.permanent == permanent ? 
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy all non-Gorgon creatures."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_CREATURE);
            for (final MagicPermanent target : targets) {
                if (!target.hasSubType(MagicSubType.Gorgon)) {
                    game.doAction(new MagicDestroyAction(target));
                }
            }
        }
    }
]
