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
                final Collection<MagicPermanent> targets = it.filterPermanents(LAND_YOU_CONTROL);
                for (final MagicPermanent land : targets) {
                    game.doAction(new MagicTapAction(land));
                }
             });
        }
    }
]
