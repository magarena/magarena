[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source,final MagicPayedCost payedCost) {
            return source.isValid() ?
                new MagicEvent(
                    source,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    new MagicDamageTargetPicker(source.getPower()),
                    this,
                    "If SN is on the battlefield, it deals damage equal to its power to target creature an opponent controls\$ " +
                    "and that creature deals damage equal to its power to SN."
                ):
                MagicEvent.NONE
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent SN = event.getPermanent();
            if (SN.isValid()) {
                event.processTargetPermanent(game, {
                    game.doAction(new DealDamageAction(
                        SN,
                        it,
                        SN.getPower()
                    ));
                    game.doAction(new DealDamageAction(
                        it,
                        SN,
                        it.getPower()
                    ));
                });
            }
        }
    }
]
