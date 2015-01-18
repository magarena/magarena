[
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return permanent.isEnemy(card) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        "Pay {1}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                    ),
                    this,
                    "PN may\$ pay {1}\$. If PN does, PN draws a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }
    }
]
