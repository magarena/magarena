[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Remove a +1/+1 counter?"),
                this,
                "PN may\$ remove a +1/+1 counter from SN. If PN doesn't, sacrifice SN" + "and it deals damage equal to the number of +1/+1 counters on it to each creature without flying and each player."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicRemoveCounterEvent(event.getPermanent(), MagicCounterType.PlusOne, 1);
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,event.getPermanent().getCounters(MagicCounterType.PlusOne));
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicDamage damage=new MagicDamage(event.getSource(),player,event.getPermanent().getCounters(MagicCounterType.PlusOne));
                game.doAction(new MagicDealDamageAction(damage));
            }
            }
        }
    }
]
