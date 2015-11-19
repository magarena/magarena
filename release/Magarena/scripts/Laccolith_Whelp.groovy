[
    new MagicWhenSelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final int power = permanent.getPower(); //May change, but gives a value for log
            return new MagicEvent(
                permanent,
                new MagicMayChoice(NEG_TARGET_CREATURE),
                new MagicDamageTargetPicker(power),
                this,
                "PN may\$ have SN deal damage equal to its power ("+power+") to target creature\$. " +
                "If PN does, SN assigns no combat damage this turn."
            );
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
