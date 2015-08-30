[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicMayChoice(
                        "Pay {2} and draw a card?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                    ),
                    this,
                    "PN may\$ pay {2}\$. If PN does, PN draws a card."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer()));
            }
        }
    }
]
