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
                "Creatures you control get +0/+1 until end of turn. " + 
                "PN may\$ pay {2}\$. If PN doesn't, creatures you control get an additional +0/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.isYes() ? 1 : 3;
            final Collection<MagicPermanent> targets = event.getRefPlayer().filterPermanents(CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target, 0, amt));
            }
        }
    }
]
