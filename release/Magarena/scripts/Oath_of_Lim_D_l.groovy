def EXCEPT_SELF = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target != source && target.isController(player);
    }
};

def SACRIFICE_EXCEPT = {
    final MagicSource source ->
    return new MagicTargetChoice(
        EXCEPT_SELF,
        "a permanent to sacrifice except ${source.getName()}"
    );
};

def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicSource source = event.getSource();
    final MagicEvent discard = new MagicDiscardEvent(source, 1);
    if (event.isYes() && discard.isSatisfied()) {
        game.addEvent(discard);
    } else {
        game.addEvent(new MagicSacrificePermanentEvent(source, event.getPlayer(), SACRIFICE_EXCEPT(source)))
    }
}

[
    new LifeIsLostTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicLifeChangeTriggerData lifeChange) {
            final int amount = lifeChange.amount;
            return permanent.isFriend(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "For each 1 life PN lost, PN may discard a card. If PN doesn't, "+
                    "he or she sacrifices a permanent other than SN. (${amount})"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int amount = event.getRefInt();
            //Discarding is optional for each point of life separately
            //If there are not enough cards to be discarded, the remaining count is sacrificed
            for (int i=amount; i>0; i--) {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer(),
                    new MagicMayChoice("Discard a card?"),
                    action,
                    "PN may\$ discard a card."
                ));
            }
        }
    }
]
