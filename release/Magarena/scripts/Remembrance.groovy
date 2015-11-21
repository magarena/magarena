[
    new WhenOtherDiesTrigger() {
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
                    "PN may\$ search his or her library for a card named RN, reveal it, and put it into his or her hand. "+
                    "If PN does, he or she shuffles his or her library."
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
