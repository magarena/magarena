def TRAPPED_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isLand() && (target.getCounters(MagicCounterType.Trap)>=1) && target.isController(player);
    }
};

def SACRIFICE_TRAPPED_LAND = new MagicTargetChoice(
    TRAPPED_LAND_YOU_CONTROL,
    MagicTargetHint.None,
    "a land with a trap counter on it to sacrifice"
);

def TRAPPED_LAND_CONDITION =new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(TRAPPED_LAND_YOU_CONTROL)>=1;
        }
    };

[
    new MagicPermanentActivation(
        [TRAPPED_LAND_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(source,SACRIFICE_TRAPPED_LAND)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE_WITHOUT_FLYING,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target attacking creature without flying\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage=new MagicDamage(event.getSource(),target,3);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
