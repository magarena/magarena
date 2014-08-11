[	
	new MagicStatic(
        MagicLayer.Ability
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return !source.getController().getOpponent().controlsPermanent(MagicSubType.Forest);
        }
    },
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(MagicSubType.Forest) == false;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new MagicPutStateTriggerOnStackAction(new MagicEvent(
                source,
                {
                    final MagicGame G, final MagicEvent E ->
                    G.doAction(new MagicSacrificeAction(E.getPermanent()));
                },
                "Sacrifice SN."
            )));
        }
    }
]
