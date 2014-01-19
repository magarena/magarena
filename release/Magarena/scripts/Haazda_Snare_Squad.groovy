[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return 
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                        new MagicPayManaCostChoice(MagicManaCost.create("{W}"))
                    ),
                    MagicTapTargetPicker.Tap,
                    this,
                    "PN may\$ pay {W}\$. If PN does, tap target creature\$."
                )
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    game.doAction(new MagicTapAction(creature,true));
                });
            }
        }
    }
]
