[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_NONHUMAN_CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Undying);
            permanent.addAbility(MagicUndyingTrigger.create());
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            return (permanent.isController(damage.getTarget()) &&
                    damage.getSource().isPermanent() &&
                    damage.getSource().hasSubType(MagicSubType.Human)) ?
                new MagicEvent(
                    permanent,
                    damage.getSource(),
                    this,
                    "Destroy RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDestroyAction(event.getRefPermanent()));
        }
    }
]
