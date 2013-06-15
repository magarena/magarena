[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isCreature() &&
                    permanent.isTapped()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicUntapAction(event.getPermanent()));
        }
    }
]
