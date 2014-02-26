def ATTACKING_HUMANS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasSubType(MagicSubType.Human);
        }
    }
[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Each attacking Human gains flying."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final Collection<MagicPermanent> targets = game.filterPermanents(ATTACKING_HUMANS);
            for (final MagicPermanent creature : targets) {
                if (creature != permanent && creature.isAttacking()) {
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Flying));
                }
            }
        }
    }
]
