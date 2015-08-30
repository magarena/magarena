[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (permanent.isUntapped() &&
                    otherPermanent.isNonToken() &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    this,
                    "PN puts a 1/1 colorless Myr artifact creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 colorless Myr artifact creature token")
            ));
        }
    }
]
