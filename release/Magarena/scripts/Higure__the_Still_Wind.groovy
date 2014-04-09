def NINJA_FILTER=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.hasSubType(MagicSubType.Ninja);
        }
    };
def TARGET_NINJA = new MagicTargetChoice(
    NINJA_FILTER,
    "target ninja"
)

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "Unblockable"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
            new MagicPayManaCostEvent(source,"{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_NINJA,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target Ninja\$ can't be blocked this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Unblockable));
            });
        }
    }
]
