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
            final MagicPlayer controller = event.getSource().getController();
            event.processTargetPlayer(game, {
                final MagicPlayer targetPlayer ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    targetPlayer,
                    new MagicMayChoice("Have Combustible Gearhulk's controller draw three cards?"),
                    {
                        final MagicGame g, final MagicEvent e ->
                        if (e.isYes()) {
                            game.doAction(new DrawAction(controller, 3));
                        } else {
                            final MagicAction millAction = new MillLibraryAction(controller, 3);
                            game.doAction(millAction);
                            final int amount = millAction.getMilledCards().collect({ it.getConvertedCost() }).inject(0, { result, i -> result + i })
                            game.doAction(new DealDamageAction(
                                e.getSource(),
                                targetPlayer,
                                amount
                            ));
                        }
                    },
                    "PN may\$ have SN's controller draw three cards. " +
                    "If PN doesn't, put the top three cards of SN's controller's library to the graveyard, " +
                    "then SN deals damage to PN equal to the total converted mana cost of those cards."
                ));
            });
        }
    }
]

