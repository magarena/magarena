[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                    otherPermanent.hasType(MagicType.Creature) && 
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent, 
                    this,
                    "PN may\$ search your library for a card named RN, reveal it, put it in your hand, and shuffle your library."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final String name = event.getRefPermanent().getName();
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicTargetChoice(
                        new MagicCardFilterImpl() {
                            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                                return target.getName().equals(name);
                            }
                            public boolean acceptType(final MagicTargetType targetType) {
                                return targetType==MagicTargetType.Library;
                            }
                        }, 
                        "a card named ${name} from your library"
                    ),
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]
