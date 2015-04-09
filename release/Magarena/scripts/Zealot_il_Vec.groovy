[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE),
                    new MagicDamageTargetPicker(1),
                    this,
                    "PN may\$ have SN deal 1 damage to target creature\$. " +
                    "If you do, prevent all combat damage SN would deal this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(new MagicDealDamageAction(permanent,it,1));
                    game.doAction(MagicChangeStateAction.Set(permanent,MagicPermanentState.NoCombatDamage));
                });
            }
        }
    }
]
