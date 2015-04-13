[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source=damage.getSource();
            return (damage.isCombat() &&
                    permanent.isFriend(source) &&
                    source.isPermanent() &&
                    (source).isCreature()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a blaze counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Blaze,1));
        }
    }
]
