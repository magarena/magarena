def choice = new MagicTargetChoice("a land an opponent controls");

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {B}{B}{B}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{B}{B}{B}"))
                ),
                this,
                "PN may\$ pay {B}{B}{B}\$. If PN doesn't, tap SN and sacrifice a land of an opponent's choice."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new TapAction(event.getPermanent()));
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    event.getPlayer().getOpponent(),
                    choice
                ));
            }
        }
    }
]
