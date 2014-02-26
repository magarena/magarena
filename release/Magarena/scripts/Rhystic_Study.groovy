[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicMayChoice(
                        "Pay {1}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                    ),
                    this,
                    "PN may\$ pay {1}\$. If PN doesn't, "+permanent.getController().toString()+" may draw a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                final MagicPermanent enchantment = event.getPermanent();
                game.addEvent(new MagicEvent(
                    enchantment,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    {
                        final MagicGame G, final MagicEvent E ->
                        if (E.isYes()) {
                            G.doAction(new MagicDrawAction(E.getPermanent().getController()));
                        }
                    },
                    enchantment.getController().toString()+" may\$ Draw a card."
                ));
                game.doAction(new MagicDrawAction(event.getPermanent().getController()));
            }
        }
    }
]
