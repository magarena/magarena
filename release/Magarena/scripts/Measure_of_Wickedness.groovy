[
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicCard card = act.card;
            return (permanent.getCard() != card && card.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    this,
                    "Target player\$ gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it, event.getPermanent()));
            })
        }
    }
]
