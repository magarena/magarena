def ATTACKING_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return (target.isController(player) && target.isCreature() && target.isAttacking());
        }
    };
def TARGET_ATTACKING_CREATURE_YOU_CONTROL = new MagicTargetChoice(
    ATTACKING_CREATURE_YOU_CONTROL,
    MagicTargetHint.Positive,
    "an attacking creature you control"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Untap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_ATTACKING_CREATURE_YOU_CONTROL,
                MagicTapTargetPicker.Untap,
                this,
                "Untap target attacking creature you control\$. " +
                "Prevent all combat damage that would be dealt to and dealt by it this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicUntapAction(creature));
                    game.doAction(MagicChangeStateAction.Set(
                        creature,
                        MagicPermanentState.NoCombatDamage
                    ));
                    final MagicPermanentList blockingCreatures = creature.getBlockingCreatures();
                    for (final MagicPermanent blocker : blockingCreatures) {
                        game.doAction(MagicChangeStateAction.Set(
                            blocker,
                            MagicPermanentState.NoCombatDamage
                        ));
                    }
                }
            });
        }
    }
]
