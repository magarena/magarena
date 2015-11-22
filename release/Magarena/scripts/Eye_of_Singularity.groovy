def exceptBasicLands = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target.isBasic() == false;
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Destroy each permanent with the same name as another permanent, except for basic lands. "+
                "They can't be regenerated."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent permanent : exceptBasicLands.filter(event)) {
                if (permanentName(permanent.getName(), Control.Any).filter(event).size() > 1) {
                    permanentName(permanent.getName(), Control.Any).filter(event) each {
                        game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                        game.doAction(new DestroyAction(it));
                    }
                }
            }
        }
    },
    
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return otherPermanent.isBasic() == false ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Destroy all other permanents named ${otherPermanent.getName()}. They can't be regenerated."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            permanentName(permanent.getName(), Control.Any).except(permanent).filter(event) each {
                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
            }
        }
    }
]
