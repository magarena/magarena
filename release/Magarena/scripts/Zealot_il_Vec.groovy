[
    new AttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE),
                    new MagicDamageTargetPicker(1),
                    this,
                    "PN may\$ have SN deal 1 damage to target creature\$. " +
                    "If PN does, prevent all combat damage SN would deal this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(new DealDamageAction(permanent,it,1));
                    game.doAction(ChangeStateAction.Set(permanent,MagicPermanentState.NoCombatDamage));
                });
            }
        }
    }
]
