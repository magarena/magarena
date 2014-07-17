[

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return !source.getController().getOpponent().controlsPermanent(MagicSubType.Island);
        }
    },
    
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                game.doAction(new MagicChangeCountersAction(
                    permanent,
                    MagicCounterType.PlusOne,
                    4,
                    true
                ));
                game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.FirstStrike,MagicStatic.Forever));
            }
            return MagicEvent.NONE;
        }
    }
]
