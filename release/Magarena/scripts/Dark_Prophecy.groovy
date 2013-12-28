[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.hasType(MagicType.Creature) && 
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card and loses 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicDrawAction(player));
            game.doAction(new MagicChangeLifeAction(player,-1));
        }
    }
]
