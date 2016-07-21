[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return spell.isFriend(permanent) && spell.hasSubType(MagicSubType.Eldrazi) && spell.hasType(MagicType.Creature) && spell.getConvertedCost() >= 7 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws two cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), 2));
        }
    }
]
