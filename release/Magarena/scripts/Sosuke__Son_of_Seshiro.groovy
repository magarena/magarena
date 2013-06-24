[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_SNAKE_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().getController() == permanent.getController() &&
                    damage.getSource().isPermanent() &&
                    damage.getSource().hasSubType(MagicSubType.Warrior) &&
                    damage.isCombat() &&
                    damage.getTarget().isPermanent() &&
                    damage.getTarget().isCreature()) ?
                new MagicEvent(
                    damage.getSource(),
                    permanent.getController(),
                    damage.getTarget(),
                    this,
                    "Destroy RN at end of combat."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(MagicChangeStateAction.Set(event.getRefPermanent(),MagicPermanentState.DestroyAtEndOfCombat));
        }
    }
]
