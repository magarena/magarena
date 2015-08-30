package magic.model.event;

import magic.model.MagicCostManaType;
import magic.model.MagicType;
import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;

/** Each source can only be activated once for mana. Each mana type must come from at most one activation. */
public class MagicSourceManaActivation {

    public final MagicPermanent permanent;
    public final MagicManaActivation[] activations;
    public boolean available;
    public MagicManaType manaType;

    public MagicSourceManaActivation(final MagicGame game,final MagicPermanent permanent) {
        this.permanent=permanent;
        activations=new MagicManaActivation[MagicManaType.NR_OF_TYPES];
        available=false;

        for (final MagicManaActivation activation: permanent.getManaActivations()) {
            if (activation.canPlay(game,permanent)) {
                available=true;
                for (final MagicManaType manaType : activation.getManaTypes()) {
                    final int idx = manaType.ordinal();
                    if (activations[idx] == null ||
                        activations[idx].getWeight() > activation.getWeight()) {
                        activations[idx] = activation;
                    }
                }

                // rule 107.4h:
                // The snow mana symbol {S} represents one generic mana in a cost.
                // This generic mana can be paid with one mana of any type produced by a snow permanent.
                // implementation:
                // Each mana ability on a snow permanent produces an additional snow mana type
                if (permanent.hasType(MagicType.Snow)) {
                    final int idx = MagicManaType.Snow.ordinal();
                    if (activations[idx] == null ||
                        activations[idx].getWeight() > activation.getWeight()) {
                        activations[idx] = activation;
                    }
                }
            }
        }
    }

    public MagicManaType canProduce(final MagicCostManaType costManaType) {
        if (available) {
            for (final MagicManaType tManaType : costManaType.getTypes()) {
                if (activations[tManaType.ordinal()]!=null) {
                    return tManaType;
                }
            }
        }
        return MagicManaType.NONE;
    }

    public void produce(final MagicGame game,final MagicCostManaType costManaType) {
        MagicManaActivation bestManaActivation=null;
        for (final MagicManaType tManaType : costManaType.getTypes()) {
            final MagicManaActivation manaActivation=activations[tManaType.ordinal()];
            if (manaActivation!=null&&(bestManaActivation==null||bestManaActivation.getWeight()>manaActivation.getWeight())) {
                bestManaActivation=manaActivation;
            }
        }

        if (bestManaActivation==null) {
            throw new IllegalStateException("This mana source cannot produce "+costManaType.getText()+".");
        }
        final MagicSourceManaActivationResult bestSourceManaActivation=
            new MagicSourceManaActivationResult(permanent,bestManaActivation);
        bestSourceManaActivation.doActivation(game);
    }

    public int getWeight() {
        return activations[manaType.ordinal()].getWeight();
    }

    public MagicSourceManaActivationResult getResult() {
        return new MagicSourceManaActivationResult(permanent,activations[manaType.ordinal()]);
    }
}
