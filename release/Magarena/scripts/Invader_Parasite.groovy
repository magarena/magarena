[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            final String cardName = permanent.getExiledCard().getName();
            return (otherPermanent.isLand() && 
                    otherPermanent.getName().equals(cardName) &&
                    otherPermanent.isEnemy(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    this,
                    "SN deals 2 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    }
]
