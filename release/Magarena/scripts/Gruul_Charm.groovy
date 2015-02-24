def EFFECT1 = MagicRuleEventAction.create("Creatures without flying can't block this turn.");

def EFFECT3 = MagicRuleEventAction.create("SN deals 3 damage to each creature with flying.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - creatures without flying can't block this turn; " +
                "or gain control of all permanents you own; " +
                "or SN deals 3 damage to each creature with flying.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.addEvent(EFFECT1.getEvent(event.getSource()));
            } else if (event.isMode(2)) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicPermanent> permanents = game.filterPermanents(player, MagicTargetFilterFactory.PERMANENT_YOU_OWN);
            for (final MagicPermanent permanent : permanents) {
                game.doAction(new MagicGainControlAction(player, permanent));
                }
            } else if (event.isMode(3)) {
                game.addEvent(EFFECT3.getEvent(event.getSource()));
            }
        }
    }
]
