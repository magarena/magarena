[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains 2 life for each other creature he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicTargetFilter<MagicPermanent> filter = CREATURE_YOU_CONTROL.except(event.getPermanent());
            final int amount = game.filterPermanents(player,filter).size();
            game.doAction(new MagicChangeLifeAction(player,amount * 2));
        }
    }
]
