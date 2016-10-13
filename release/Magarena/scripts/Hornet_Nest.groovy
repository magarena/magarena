[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    damage.getDealtAmount(),
                    this,
                    "PN creates RN 1/1 green Insect creature tokens with flying and deathtouch."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 green Insect creature token with flying and deathtouch"),
                event.getRefInt()
            ));
        }
    }
]
