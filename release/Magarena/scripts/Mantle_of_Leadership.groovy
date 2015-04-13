[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isCreature() ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent(),
                    this,
                    "RN get +2/+2 until end of turn."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getRefPermanent(),2,2));
        }
    }
]
