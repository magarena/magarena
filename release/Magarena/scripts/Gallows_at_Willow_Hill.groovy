def UNTAPPED_HUMAN_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Human) &&
               target.isUntapped() &&
               target.isController(player);
    }
};

def choice = new MagicTargetChoice(UNTAPPED_HUMAN_YOU_CONTROL, "an untapped Human you control");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{3}"),
                new MagicRepeatedPermanentsEvent(source, choice, 3, MagicChainEventFactory.Tap)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature.\$ Its controller creates a 1/1 white Spirit creature token with flying."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.doAction(new PlayTokenAction(
                    it.getController(),
                    CardDefinitions.getToken("1/1 white Spirit creature token with flying")
                ));
            });
        }
    }
]
