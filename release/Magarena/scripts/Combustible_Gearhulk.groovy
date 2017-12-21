[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Choose target player\$."
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            controller = event.getSource().getController();
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Have SN's controller draw three cards?"),
                    {
                        final MagicGame g, final MagicEvent e ->
                        if (e.isYes()) {
                            g.doAction(new DrawAction(controller, 3));
                        }
                        else {
                            millAction = new MillLibraryAction(controller, 3);
                            g.doAction(millAction);
                            g.doAction(new DealDamageAction(
                                e.getSource(),
                                it,
                                millAction.getMilledCards().collect({ it.getConvertedCost() }).inject(0, { result, i -> result + i });
                            );
                        }
                    },
                    "That player may\$ have PN draw three cards. " +
                    "If the player doesn't, put the top three cards of PN's library to the graveyard, " +
                    "then SN deals damage to that player equal to the total converted mana cost of those cards."
            });
        }
    }
]

