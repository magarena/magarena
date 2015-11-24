[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicMayChoice(
                        "Pay {1}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                    ),
                    this,
                    "PN may\$ pay {1}\$. If PN doesn't, PN sacrifices a permanent."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    event.getPlayer(),
                    SACRIFICE_PERMANENT
                ));
            }
        }
    }
]
