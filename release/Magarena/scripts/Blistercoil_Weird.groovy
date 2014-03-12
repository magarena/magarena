[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.isInstantOrSorcerySpell()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+1 until end of turn. Untap SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),1,1));
            game.doAction(new MagicUntapAction(event.getPermanent()));
        }
    }
]
