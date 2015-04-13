[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains 2 life for each Shrine he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = 2 * event.getPlayer().getNrOfPermanents(MagicSubType.Shrine);
            game.doAction(new ChangeLifeAction(event.getPlayer(),amt));
        }
    }
]
