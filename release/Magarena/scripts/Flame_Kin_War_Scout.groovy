[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent && 
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Sacrifice SN. If you do, SN deals 4 damage to RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSacrificeAction sac = new MagicSacrificeAction(event.getPermanent());
            game.doAction(sac);
            if (sac.isValid()) {
                game.doAction(new DealDamageAction(
                    event.getPermanent(),
                    event.getRefPermanent(),
                    4
                ));
            }
        }
    }
]
