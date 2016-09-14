def LEGENDARY_SPIRIT_PERMANENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Spirit) &&
                   target.hasType(MagicType.Legendary) &&
                   (target.hasType(MagicType.Creature) ||
                    target.hasType(MagicType.Enchantment) ||
                    target.hasType(MagicType.Artifact) ||
                    target.hasType(MagicType.Land) ||
                    target.hasType(MagicType.Planeswalker));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

def A_LEGENDARY_SPIRIT_PERMANENT_FROM_LIBRARY = new MagicTargetChoice(
    LEGENDARY_SPIRIT_PERMANENT_CARD_FROM_LIBRARY,
    "a Legendary Spirit permanent card"
);

def SACRIFICE_SPIRIT = new MagicTargetChoice("a Spirit creature to sacrifice");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Search"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicRepeatedPermanentsEvent(source, SACRIFICE_SPIRIT, 3, MagicChainEventFactory.Sac)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a legendary Spirit permanent card and puts it onto the battlefield. "+
                "Then shuffles his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                A_LEGENDARY_SPIRIT_PERMANENT_FROM_LIBRARY
            ));
        }
    }
]
