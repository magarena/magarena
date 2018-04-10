[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (equippedCreature.isValid() && otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a +1/+1 counter on equipped creature. " +
                    "If equipped creature is a Vampire, put two +1/+1 counters on it instead."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent equippedCreature = event.getPermanent().getEquippedCreature();
            final int amount = equippedCreature.hasSubType(MagicSubType.Vampire) ? 2 : 1;
            game.doAction(new ChangeCountersAction(event.getPlayer(),equippedCreature,MagicCounterType.PlusOne,amount));
        }
    }
]
