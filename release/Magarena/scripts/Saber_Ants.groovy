[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.PLAY_TOKEN,
                        damage.getDealtAmount(),
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    amount,
                    this,
                    "PN may\$ put RN 1/1 green Insect creature tokens onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
             if (event.isYes()){
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 green Insect creature token"),
                    event.getRefInt()
                ));
            }
        }
    }
]
