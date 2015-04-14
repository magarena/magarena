[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.DRAW_CARDS,
                    1,
                    MagicSimpleMayChoice.DEFAULT_NONE
                ),
                this,
                "You may\$ draw a card. If you do, discard a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player=event.getPlayer();
                game.doAction(new DrawAction(player));
                game.addEvent(new MagicDiscardEvent(event.getSource(),player));
            }
        }
    }
]
