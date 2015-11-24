[
    new MonstrousTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeStateAction action) {
            return action.permanent == permanent ?
                new MagicEvent(
                    permanent,
                    this,
                    "Each player sacrifices 3 lands."
                ):
            MagicEvent.NONE
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                for (int i=3;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        SACRIFICE_LAND
                    ));
                }
            }
        }
    }
]
