[
    new WhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (died.isCreature() && permanent.isOpponent(died.getOwner())) ?
                new MagicEvent(
                    permanent,
                    died,
                    this,
                    "PN gains life equal to RN's toughness."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefPermanent().getToughness()
            game.logAppendValue(event.getPlayer(),amount);
            game.doAction(new ChangeLifeAction(event.getPlayer(),event.getRefPermanent().getToughness()));
        }
    }
]
