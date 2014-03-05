[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.getEnchantedPermanent()==creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    permanent.getEnchantedPermanent().toString()+" gets +X/+0 until end of turn where X is PN's devotion to red."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature=event.getPermanent().getEnchantedPermanent();
            final int power = event.getPermanent().getController().getDevotion(MagicColor.Red);
            game.doAction(new MagicChangeTurnPTAction(creature,power,0));
        }
    }
]
