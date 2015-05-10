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
            final int amount = CREATURE_YOU_CONTROL.except(event.getPermanent()).filter(event).size();
            game.doAction(new ChangeLifeAction(player,amount * 2));
        }
    }
]
