def SACRIFICE_ATTACKING_CREATURE = new MagicTargetChoice("an attacking creature to sacrifice");

def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new SacrificeAction(it));
        game.doAction(new PlayTokensAction(
            event.getRefPlayer(),
            CardDefinitions.getToken("1/1 white Soldier creature token"),
            it.getToughness()
        ));
    });
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ sacrifices an attacking creature. "+
                "PN creates X 1/1 white Soldier creature tokens, where X is that creature's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    SACRIFICE_ATTACKING_CREATURE,
                    MagicSacrificeTargetPicker.create(),
                    event.getPlayer(),
                    action,
                    "Choose an attacking creature to sacrifice\$."
                ));
            });
        }
    }
]
