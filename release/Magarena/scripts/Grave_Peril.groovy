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
                    "Sacrifice SN. If you do, destroy RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final SacrificeAction sac = new SacrificeAction(event.getPermanent());
            game.doAction(sac);
            if (sac.isValid()) {
                game.doAction(new DestroyAction(event.getRefPermanent()));
            }
        }
    }
]
