def NINJA_FILTER=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.hasSubType(MagicSubType.Ninja);
        }
    };
def TARGET_NINJA = new MagicTargetChoice(
    NINJA_FILTER,
    "target ninja"
)

def NINJA_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Ninja);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def A_NINJA_CARD_FROM_LIBRARY = new MagicTargetChoice(
    NINJA_CARD_FROM_LIBRARY,
    "a ninja card"
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
                "Target Ninja\$ is unblockable this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Unblockable));
            });
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                damage.getTarget().isPlayer() &&
                damage.isCombat()) ?
                new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may search his or her library for a ninja card, reveals it, puts it into PN's Hand, and shuffles PN's library."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchIntoHandEvent(
                event,
                A_NINJA_CARD_FROM_LIBRARY
            ));
            }
        }
    }
]
