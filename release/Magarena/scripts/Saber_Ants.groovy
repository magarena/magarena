[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(),
                    amount,
                    this,
                    "PN may\$ create RN 1/1 green Insect creature tokens."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 green Insect creature token"),
                    event.getRefInt()
                ));
            }
        }
    }
]
