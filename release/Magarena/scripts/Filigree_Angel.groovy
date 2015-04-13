[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    permanent,
                    this,
                    "PN gains 3 life for each artifact PN controls."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 3 * game.filterPermanents(player,ARTIFACT_YOU_CONTROL).size();
            game.doAction(new ChangeLifeAction(player,amount));
        }
    }
]
