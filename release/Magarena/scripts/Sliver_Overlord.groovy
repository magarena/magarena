def TARGET_SLIVER_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Sliver);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };
[
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
           return new MagicEvent(
                source,
                this,
                "PN may search his or her library for a Sliver card, reveal it, put it into his or her hand, and shuffle his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                TARGET_SLIVER_CARD_FROM_LIBRARY,
                "target Sliver");
			game.addEvent(new MagicSearchIntoHandEvent(
                event,
                targetChoice
            ));
        }
    },
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
			return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target Sliver creature"),
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target Sliver creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent perm ->
                game.doAction(new MagicGainControlAction(
                    event.getPlayer(),
                    perm
                ));
            } as MagicPermanentAction);
        }
    }
]