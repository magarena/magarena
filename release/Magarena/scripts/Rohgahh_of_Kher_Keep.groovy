def filter = MagicTargetFilterFactory.permanentName("Kobolds of Kher Keep", Control.You);
[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{R}{R}{R}"))
                ),
                this,
                "PN may\$ pay {R}{R}{R}. If PN doesn't, tap SN and all creatures named Kobolds of Kher Keep, "+
                "then an opponent gains control of them."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                final MagicPlayer player = event.getPlayer();
                final MagicPlayer opponent = player.getOpponent();
                final MagicPermanent permanent = event.getPermanent();
                game.doAction(new TapAction(permanent));
                game.doAction(new GainControlAction(opponent, permanent));
                filter.filter(player) each {
                    game.doAction(new TapAction(it));
                    game.doAction(new GainControlAction(opponent, it));
                }
            }
        }
    }
]
