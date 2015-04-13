[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.getEnchantedPermanent()==creature) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent(),
                    this,
                    "RN gets +X/+0 until end of turn, where X is PN's devotion to red."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature=event.getRefPermanent();
            final int power = event.getPlayer().getDevotion(MagicColor.Red);
            game.doAction(new ChangeTurnPTAction(creature,power,0));
        }
    }
]
