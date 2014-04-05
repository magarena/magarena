[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{B}{B}{B}"))
                    ),
                    this,
                    "PN taps SN and sacrifices a land of an opponent's choice, unless he or she pays {B}{B}{B}.\$"
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new MagicTapAction(event.getPermanent(),true));
                game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),event.getPermanent().getOpponent(),new MagicTargetChoice("a land an opponent controls")));
            }
        }
    }
]
