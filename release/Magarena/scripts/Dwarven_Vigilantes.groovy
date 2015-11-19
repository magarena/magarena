[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE),
                    new MagicDamageTargetPicker(permanent.getPower()),
                    this,
                    "PN may\$ have SN deal damage equal to its power to target creature\$. " +
                    "If PN does, SN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(new DealDamageAction(permanent,it,permanent.getPower()));
                    game.doAction(ChangeStateAction.Set(permanent,MagicPermanentState.NoCombatDamage));
                });
            }
        }
    }
]
