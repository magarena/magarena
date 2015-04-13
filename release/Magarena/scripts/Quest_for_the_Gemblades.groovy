[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source=damage.getSource();
            final MagicTarget target=damage.getTarget();
            return (damage.isCombat() &&
                    permanent.isFriend(source) &&
                    source.isPermanent() &&
                    target.isPermanent() &&
                    (source).isCreature() &&
                    target.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_POS_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a quest counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Quest,1));
            }
        }
    }
]
