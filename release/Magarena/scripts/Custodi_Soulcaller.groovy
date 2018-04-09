def filter = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
        return target.hasType(MagicType.Creature) && target.getConvertedCost() <= 1;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Graveyard;
    }
};

def choice = new MagicTargetChoice(filter, "a creature card with converted mana cost 1 or less from your graveyard");

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                choice,
                this,
                "PN return target creature card with converted mana cost X or less from his or her graveyard to the battlefield, where X is the number of players he or she attacked with a creature this combat."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                    game.doAction(new ReanimateAction(it,event.getPlayer()));
            });
        }
    }
]
