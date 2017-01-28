def NONLAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return !(target.hasType(MagicType.Land));
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};
def A_NONLAND_CARD_FROM_HAND = new MagicTargetChoice(
    NONLAND_CARD_FROM_HAND,
    MagicTargetHint.None,
    "a nonland card from your hand"
);
[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    A_NONLAND_CARD_FROM_HAND
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ cast a nonland card\$ from his or her hand without paying its mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(CastCardAction.WithoutManaCost(
                        event.getPlayer(),
                        it,
                        MagicLocationType.OwnersHand,
                        MagicLocationType.Graveyard
                    ));
                });
            }
        }
    }
]
