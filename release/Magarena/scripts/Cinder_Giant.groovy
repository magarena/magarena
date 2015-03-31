[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals 2 damage to each other creature PN controls."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    creature
                )
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicDealDamageAction(creature,target,2));
            }
        }
    }
]
