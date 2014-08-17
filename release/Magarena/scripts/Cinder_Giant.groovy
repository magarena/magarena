[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 2 damage to each other creature PN controls."
                ):
            MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                creature.getController(),
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    creature
                )
            );
            for (final MagicPermanent target : targets) {
                final MagicDamage damage = new MagicDamage(creature,target,2);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
