def choiceAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getRefPlayer();
    final MagicPlayer opponent = event.getPlayer();
    if (event.isYes()) {
        game.doAction(new DrawAction(player, 3));
    } else {
        final MagicAction millAction = new MillLibraryAction(player, 3);
        game.doAction(millAction);
        final int amount = (int)millAction.getMilledCards()*.getConvertedCost().sum();
        game.doAction(new DealDamageAction(
            event.getSource(),
            opponent,
            amount
        ));
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Choose target player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Have ${event.getPlayer()} draw three cards?"),
                    event.getPlayer(),
                    choiceAction,
                    "PN may\$ have RN draw three cards. " +
                    "If PN doesn't, put the top three cards of RN's library to the graveyard, " +
                    "then SN deals damage to PN equal to the total converted mana cost of those cards."
                ));
            });
        }
    }
]

