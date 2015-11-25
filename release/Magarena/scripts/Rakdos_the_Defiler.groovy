def NON_DEMON_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && !target.hasSubType(MagicSubType.Demon)
    } 
};

def A_NON_DEMON_PERMANENT_YOU_CONTROL = new MagicTargetChoice(
    NON_DEMON_PERMANENT_YOU_CONTROL,
    "a non-Demon permanent to sacrifice"
);

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices half the non-Demon permanents he or she controls, rounded up."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicRepeatedPermanentsEvent(
                event.getSource(),
                A_NON_DEMON_PERMANENT_YOU_CONTROL,
                (int)Math.ceil(event.getPlayer().getNrOfPermanents(NON_DEMON_PERMANENT_YOU_CONTROL)/2),
                MagicChainEventFactory.Sac
            ));
        }
    },
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "PN sacrifices half the non-Demon permanents he or she controls, rounded up."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int n = (int)Math.ceil(event.getPlayer().getNrOfPermanents(NON_DEMON_PERMANENT_YOU_CONTROL)/2);
            for (int i=n;i>0;i--) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    event.getPlayer(),
                    A_NON_DEMON_PERMANENT_YOU_CONTROL
                ));
            }      
        }
    }
]
