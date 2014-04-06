[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Creatures PN controls get +1/+1 and gain trample until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
            }
        }
    }
]
