[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS
                    ),
                    new MagicDestroyTargetPicker(false),
                    this,
                    "PN may\$ destroy target artifact\$."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicDestroyAction(target));
                    }
                });
                game.doAction(MagicChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.NoCombatDamage
                ));
            }
        }
    }
]
