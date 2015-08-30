[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                eotPlayer,
                this,
                "SN deals X damage to PN, where X is the number of untapped lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(UNTAPPED_LAND_YOU_CONTROL);
            game.doAction(new DealDamageAction(
                event.getSource(),
                event.getPlayer(),
                amount
            ));
            game.logAppendX(event.getPlayer(),amount);
        }
    }
]
