def choice = MagicTargetChoice.Positive("target creature card from a graveyard")

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Exile"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{B}{G}"),
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                payedCost.getTarget(),
                this,
                "Exile target creature card\$ from a graveyard. " +
                "Its owner creates a 1/1 black and green Snake enchantment creature token with deathtouch."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicPlayer player = event.getPlayer();
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
                game.doAction(new PlayTokensAction(it.getController(), CardDefinitions.getToken("1/1 black and green Snake enchantment creature token with deathtouch"), 1));
            });
        }
    },
    new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags & ~MagicType.Creature.getMask();
        }
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.remove(MagicSubType.God);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().getDevotion(MagicColor.Black, MagicColor.Green) < 7;
        }
    }
]

