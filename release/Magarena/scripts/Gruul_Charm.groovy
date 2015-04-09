def EFFECT1 = MagicRuleEventAction.create("Creatures without flying can't block this turn.");

def EFFECT3 = MagicRuleEventAction.create("SN deals 3 damage to each creature with flying.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ - creatures without flying can't block this turn; " +
                "or gain control of all permanents you own; " +
                "or SN deals 3 damage to each creature with flying.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(2)) {
                final MagicPlayer player = event.getPlayer();
                final List<MagicPermanent> permanents = player.filterPermanents(PERMANENT_YOU_OWN);
                for (final MagicPermanent permanent : permanents) {
                    game.doAction(new MagicGainControlAction(player, permanent));
                }
            } else {
                event.executeModalEvent(game, EFFECT1, EFFECT1, EFFECT3);
            }
        }
    }
]
