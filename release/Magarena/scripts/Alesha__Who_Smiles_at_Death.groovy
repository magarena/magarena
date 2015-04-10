def CREATURE_WITH_POWER_2_OR_OR_LESS_FROM_GRAVEYARD = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game, final MagicPlayer player, final MagicCard target) {
        return target.hasType(MagicType.Creature) && target.getPower() <= 2;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Graveyard;
    }
};

def A_CREATURE_WITH_POWER_2_OR_OR_LESS_FROM_GRAVEYARD = new MagicTargetChoice(
    CREATURE_WITH_POWER_2_OR_OR_LESS_FROM_GRAVEYARD,
    MagicTargetHint.None,
    "a creature with power 2 or less from your graveyard"
);

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{W/B}{W/B}")),
                    A_CREATURE_WITH_POWER_2_OR_OR_LESS_FROM_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ pay {W/B}{W/B}\$. If you do, return target creature card\$ with power 2 or less from your graveyard to the battlefield tapped and attacking."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new MagicReanimateAction(
                        it,
                        event.getPlayer(),
                        [MagicPlayMod.TAPPED, MagicPlayMod.ATTACKING]
                    ));
                });
            }
        }
    }
]
