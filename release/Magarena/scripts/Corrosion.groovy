[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    this,
                    "PN puts a rust counter on each artifact target opponent\$ controls. "+
                    "Then destroys each artifact with converted mana cost less than or equal to the number of rust counters on it. "+
                    "Artifacts destroyed this way can't be regenerated."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                ARTIFACT_YOU_CONTROL.filter(it) each {
                    game.doAction(new ChangeCountersAction(it, MagicCounterType.Rust, 1));
                }
                ARTIFACT_YOU_CONTROL.filter(it) each {
                    if (it.getConvertedCost() <= it.getCounters(MagicCounterType.Rust)) {
                        game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                        game.doAction(new DestroyAction(it));
                    }
                }
            });
        }
    }
]
