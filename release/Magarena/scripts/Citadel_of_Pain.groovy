[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer endofturnPlayer) {
            return new MagicEvent(
                permanent,
                endofturnPlayer,
                this,
                "SN deals X damage to PN, where X is the number of untapped lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicTargetFilterFactory.UNTAPPED_LAND_YOU_CONTROL);
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),amount);
            game.doAction(new MagicDealDamageAction(damage));
            game.logAppendMessage(event.getPlayer(),"("+amount+")");
        }
    }
]
