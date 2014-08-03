[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each other Shaman creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> shamans = game.filterPermanents(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.SHAMAN_CREATURE_YOU_CONTROL,
                    event.getPermanent()
                )
            );
            for (final MagicPermanent creature : shamans) {
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1));
            }
        }
    },
    
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return creature.isFriend(permanent) &&
                creature.hasCounters(MagicCounterType.PlusOne) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.NEG_TARGET_PLAYER
                    ),
                    new MagicDamageTargetPicker(1),
                    creature,
                    this,
                    "PN may\$ have SN creature deal 1 damage to target player\$."
                ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game, {
                    final MagicPermanent permanent = event.getRefPermanent()
                    final MagicDamage damage = new MagicDamage(permanent,it,1);
                    game.doAction(new MagicDealDamageAction(damage));
                });
            }
        }
    }
]
