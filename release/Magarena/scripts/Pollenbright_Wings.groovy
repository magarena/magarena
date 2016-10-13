[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return damage.getTarget().isPlayer() && damage.isCombat() && damage.getSource() == permanent.getEnchantedPermanent() ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "PN creates ${damage.getDealtAmount()} 1/1 green Saproling creature tokens."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 green Saproling creature token"),
                event.getRefInt()
            ));
        }
    }
]
