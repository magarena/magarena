[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return 
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{W}")),
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS
                    ),
                    MagicTapTargetPicker.Tap,
                    this,
                    "PN may\$ pay {W}\$. If PN does, tap target creature\$ an opponent controls."
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
