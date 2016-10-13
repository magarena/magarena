def UNTAPPED_FOREST_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return  target.isController(player) && target.isUntapped() && target.hasSubType(MagicSubType.Forest);
        }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player creates a 1/1 green Cat creature token for each untapped Forest he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = UNTAPPED_FOREST_YOU_CONTROL.filter(player).size();
                game.doAction(new PlayTokensAction(
                    player,
                    CardDefinitions.getToken("1/1 green Cat creature token"),
                    amount
                ));
            }
        }
    }
]
