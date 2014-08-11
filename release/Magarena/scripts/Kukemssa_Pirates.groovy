[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.NEG_TARGET_ARTIFACT),
                    new MagicDamageTargetPicker(permanent.getPower()),
                    this,
                    "PN may\$ gain control of target artifact\$ controlled by opponent. " +
                    "If you do, SN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(MagicChangeStateAction.Set(
                        permanent,
                        MagicPermanentState.NoCombatDamage
                    ));
				game.doAction(new MagicGainControlAction(event.getPlayer(),it));
                });
            }
        }
    }
]
