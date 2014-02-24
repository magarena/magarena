[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Creatures you control gain protection from black until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainAbilityAction(target,MagicAbility.ProtectionFromBlack));
            }
        }
    }
]
