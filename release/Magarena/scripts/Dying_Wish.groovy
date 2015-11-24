[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return enchantedPermanent == died ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    enchantedPermanent,
                    this,
                    "Target player\$ loses X life and PN gains X life, where X is RN's power. (X="+
                    enchantedPermanent.getPower()+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefPermanent().getPower();
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it,-amount));
                game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
            });
        }
    }
]
