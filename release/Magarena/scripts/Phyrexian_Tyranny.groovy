[
    new OtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return new MagicEvent(
                permanent,
                card.getController(),
                new MagicMayChoice(
                    "Pay {2}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                ),
                this,
                "PN may\$ pay {2}\$. If PN doesn't, PN loses 2 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
            }
        }
    }
]
