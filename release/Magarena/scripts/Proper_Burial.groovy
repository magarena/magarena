[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int toughness = otherPermanent.getToughness();
            return (otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent) &&
                    toughness > 0) ?
                new MagicEvent(
                    permanent,
                    toughness,
                    this,
                    "PN gains " + toughness + " life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    }
]
