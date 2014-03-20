def SKELETON_VAMPIRE_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return (target.hasSubType(MagicSubType.Skeleton) ||
                    target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Zombie));
        }
    };
def TARGET_SKELETON_VAMPIRE_OR_ZOMBIE = new MagicTargetChoice(
    SKELETON_VAMPIRE_OR_ZOMBIE,  
    MagicTargetHint.Negative,
    "target Skeleton, Vampire or Zombie"
);    
[
new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_SKELETON_VAMPIRE_OR_ZOMBIE,
                this,
                "Exile target Skeleton, Vampire, or Zombie\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
            });
        }
    }
]
