[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() && permanent.isEnemy(otherPermanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN gains life equal to RN's toughness."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefPermanent().getToughness()
            game.logAppendMessage(event.getPlayer(),"("+amount+")");
            game.doAction(new ChangeLifeAction(event.getPlayer(),event.getRefPermanent().getToughness()));
        }
    }
]
