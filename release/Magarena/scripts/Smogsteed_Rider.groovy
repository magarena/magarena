[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            new MagicEvent(
                permanent,
                this,
                "Each other attacking creature gains fear until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.ATTACKING_CREATURE,
                event.getPermanent()
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainAbilityAction(target,MagicAbility.Fear));
            }
        }
    }
]
