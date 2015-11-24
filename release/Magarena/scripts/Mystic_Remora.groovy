[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) && cardOnStack.hasType(MagicType.Creature) == false ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicMayChoice(
                        "Pay {4}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{4}"))
                    ),
                    permanent.getController(),
                    this,
                    "PN may\$ pay {4}\$. If PN doesn't, RN may draw a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.addEvent(MagicRuleEventAction.create("You may draw a card.").getEvent(event.getSource()));
            }
        }
    }
]
