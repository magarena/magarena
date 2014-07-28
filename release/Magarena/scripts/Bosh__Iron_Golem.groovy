[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{R}"),
                new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_ARTIFACT)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to the sacrificed artifact's converted mana cost to target creature or player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPermanent sacrificed=event.getRefPermanent();
                final MagicDamage damage=new MagicDamage(event.getSource(),it,sacrificed.getConvertedCost());
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
