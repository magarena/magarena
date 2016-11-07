def NON_DEMON_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && !target.hasSubType(MagicSubType.Demon)
    }
};

def A_NON_DEMON_PERMANENT_YOU_CONTROL = new MagicTargetChoice(
    NON_DEMON_PERMANENT_YOU_CONTROL,
    "a non-Demon permanent to sacrifice"
);

def EVENT_ACTION = {
    final MagicGame game, final MagicEvent event ->
    def amt = (event.getPlayer().getNrOfPermanents(NON_DEMON_PERMANENT_YOU_CONTROL) + 1).intdiv(2);
    game.addEvent(new MagicRepeatedPermanentsEvent(
        event.getSource(),
        event.getPlayer(),
        A_NON_DEMON_PERMANENT_YOU_CONTROL,
        amt,
        MagicChainEventFactory.Sac
    ));
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                EVENT_ACTION,
                "PN sacrifices half the non-Demon permanents he or she controls, rounded up."
            );
        }
    },
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                EVENT_ACTION,
                "PN sacrifices half the non-Demon permanents he or she controls, rounded up."
            );
        }
    }
]
