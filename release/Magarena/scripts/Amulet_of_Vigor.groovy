[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isTapped() && otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Untap RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new UntapAction(event.getRefPermanent()));
        }
    }
]
