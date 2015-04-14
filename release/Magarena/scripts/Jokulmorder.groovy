[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice five lands?"),
                this,
                "PN may\$ sacrifice five lands. If PN doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicRepeatedPermanentsEvent(
                event.getSource(),
                SACRIFICE_LAND,
                5,
                MagicChainEventFactory.Sac
                );  
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent other) {
            return (other.hasSubType(MagicSubType.Island) &&
                    other.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap SN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new UntapAction(event.getPermanent()));
        }
    }
]
