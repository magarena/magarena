[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.hasType(MagicType.Creature) && cardOnStack.isEnemy(permanent)) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicMayChoice(
                        "Pay {2}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                    ),
                    this,
                    "PN may\$ pay {2}\$. If PN doesn't, PN loses 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
            }
        }
    }
]
