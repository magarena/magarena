[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return enchantedPermanent == died ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    "PN creates X 1/1 white Soldier creature tokens, where X is RN's power. (X="+
                    enchantedPermanent.getPower()+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white Soldier creature token"),
                event.getRefPermanent().getPower()
            ));
        }
    }
]
