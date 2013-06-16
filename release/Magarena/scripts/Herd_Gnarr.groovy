[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public boolean accept(final MagicPermanent source, final MagicPermanent other) {
            return other != source &&
                   other.isCreature() &&
                   other.isFriend(source);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent source, final MagicPermanent other) {
            return new MagicEvent(
                source,
                this,
                "SN gets +2/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),2,2));
        }
    }
]
