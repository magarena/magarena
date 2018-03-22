[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int amount = otherPermanent.hasSubType(MagicSubType.Swamp) ? 2 : 1;
            return (otherPermanent.isLand() && otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Other creatures PN control get +RN/+0 until end of turn."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
                CREATURE_YOU_CONTROL.except(event.getPermanent()).filter(event) each {
                    game.doAction(new ChangeTurnPTAction(it,amount,0));
            }
        }
    }
]
