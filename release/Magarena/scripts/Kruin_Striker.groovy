[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+0 and gains trample until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicChangeTurnPTAction(permanent,1,0));
            game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Trample));
        }
    }
]
