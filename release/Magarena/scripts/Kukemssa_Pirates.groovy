[
    new AttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS),
                    MagicExileTargetPicker.create(),
                    this,
                    "PN may\$ gain control of target artifact defending player controls.\$ " +
                    "If PN does, SN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new GainControlAction(event.getPlayer(),it));
                    game.doAction(ChangeStateAction.Set(
                        event.getPermanent(),
                        MagicPermanentState.NoCombatDamage
                    ));
                });
            }
        }
    }
]
