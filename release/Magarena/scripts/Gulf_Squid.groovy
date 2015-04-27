[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER,
                this,
                "Tap all lands target player\$ controls."
            );
        }

         @Override
         public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                LAND_YOU_CONTROL.filter(it) each {
                    final MagicPermanent land ->
                    game.doAction(new TapAction(land));
                }
            });
        }
    }
]
