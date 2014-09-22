[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.hasColor(MagicColor.Black) == false &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "When a nonblack creature enters the battlefield, sacrifice SN. If you do, destroy that creature."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSacrificeAction sac = new MagicSacrificeAction(event.getPermanent());
            game.doAction(sac);
            if (sac.isValid()) {
                game.doAction(new MagicDestroyAction(event.getRefPermanent()));
            }
        }
    }
]
