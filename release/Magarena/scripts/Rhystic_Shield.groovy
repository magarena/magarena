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
                this,
                "PN may\$ pay {2}\$.If you do, ${cardOnStack} gives +0/+1 to it's controller's creatures until end of turn. " +
                "If you don't, they get an additional +0/+2."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = event.getPlayer().getOpponent().filterPermanents(MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            if (event.isYes()) {
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicChangeTurnPTAction(target, 0, 1));
                }
            } else {
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicChangeTurnPTAction(target, 0, 3));
                }
            }
        }
    }
]
