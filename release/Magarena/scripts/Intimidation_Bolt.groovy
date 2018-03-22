[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(3,true),
                this,
                "SN deals 3 damage to target creature\$. "+
                "Other creatures can't attack this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            event.processTargetPermanent(outerGame, {
                    outerGame.doAction(new DealDamageAction(event.getSource(),it,3));
                outerGame.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                    @Override
                    public void modGame(final MagicPermanent source, final MagicGame game) {
                CREATURE.except(it).filter(it) each {
                            it.addAbility(MagicAbility.CannotAttack);
                        }
                    }
                }))
            });
        }
    }
]
