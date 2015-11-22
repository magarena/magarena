[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                payedCost.getKicker(),
                this,
                payedCost.isKicked() ?
                    "PN draws three cards." :
                    "PN draws three cards. Then discards three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new DrawAction(player,3));
            if (event.getRefInt() == 0) {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(),player,3));
            }
        }
    }
]
