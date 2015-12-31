[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                new MagicMayChoice(
                    "Pay {2}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                ),
                cardOnStack.getController(),
                this,
                "Creatures RN controls get +0/+1 until end of turn. " +
                "PN may\$ pay {2}\$. If PN doesn't, creatures RN controls get an additional +0/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.isYes() ? 1 : 3;
            CREATURE_YOU_CONTROL.filter(event.getRefPlayer()) each {
                game.doAction(new ChangeTurnPTAction(it, 0, amt));
            }
        }
    }
]
