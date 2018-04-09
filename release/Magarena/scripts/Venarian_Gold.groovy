[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                permanent,
                amount,
                this,
                "PN taps enchanted creature and puts X sleep counters on it. (X=RN)"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchanted = event.getPermanent().getEnchantedPermanent();
            game.doAction(new TapAction(enchanted));
            game.doAction(new ChangeCountersAction(event.getPlayer(), enchanted, MagicCounterType.Sleep, event.getRefInt()));
        }
    },

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.DoesNotUntap);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.getCounters(MagicCounterType.Sleep) > 0;
        }
    },

    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return upkeepPlayer == enchanted.getController() ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    enchanted,
                    this,
                    "PN removes a sleep counter from RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getRefPermanent(),MagicCounterType.Sleep,-1));
        }
    }
]
