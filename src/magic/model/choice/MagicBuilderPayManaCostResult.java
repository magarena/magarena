package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicCopyMap;
import magic.model.MagicPlayer;
import magic.model.MurmurHash3;
import magic.model.event.MagicSourceManaActivation;
import magic.model.event.MagicSourceManaActivationResult;

import java.util.Arrays;
import java.util.List;

public class MagicBuilderPayManaCostResult implements MagicPayManaCostResult, Comparable<MagicBuilderPayManaCostResult> {

    private MagicSourceManaActivationResult[] results;
    private short[] amountLeft;
    private int weight;
    private int count;
    private int x;
    private int hashCode;

    MagicBuilderPayManaCostResult(final List<MagicSourceManaActivation> sourceActivations) {
        count=0;
        x=0;
        amountLeft=new short[MagicManaType.NR_OF_TYPES];
        for (final MagicSourceManaActivation activation : sourceActivations) {
            if (activation.available) {
                for (int index=0;index<MagicManaType.NR_OF_TYPES;index++) {
                    if (activation.activations[index]!=null) {
                        amountLeft[index]++;
                    }
                }
            } else {
                count++;
                weight+=activation.getWeight();
            }
        }
        hashCode=Arrays.hashCode(amountLeft);
    }

    private MagicBuilderPayManaCostResult() {}

    private MagicBuilderPayManaCostResult(final MagicCopyMap copyMap, final MagicBuilderPayManaCostResult source) {
        results    = copyMap.copyObjects(source.results, MagicSourceManaActivationResult.class);
        amountLeft = Arrays.copyOf(source.amountLeft,source.amountLeft.length);
        weight     = source.weight;
        count      = source.count;
        x          = source.x;
        hashCode   = source.hashCode;
    }

    @Override
    public MagicBuilderPayManaCostResult copy(final MagicCopyMap copyMap) {
        return new MagicBuilderPayManaCostResult(copyMap, this);
    }

    @Override
    public MagicBuilderPayManaCostResult map(final MagicGame game) {
        final MagicBuilderPayManaCostResult result=new MagicBuilderPayManaCostResult();
        result.results=new MagicSourceManaActivationResult[results.length];
        for (int index=0;index<results.length;index++) {
            result.results[index]=results[index].map(game);
        }
        result.amountLeft=Arrays.copyOf(amountLeft,amountLeft.length);
        result.weight=weight;
        result.count=count;
        result.x=x;
        result.hashCode=hashCode;
        return result;
    }

    /** Finishes construction when needed. */
    void buildResults(final List<MagicSourceManaActivation> sourceActivations,final MagicBuilderManaCost cost) {
        x=cost.getX(count);
        results=new MagicSourceManaActivationResult[count];
        int index=0;
        for (final MagicSourceManaActivation activation : sourceActivations) {
            if (!activation.available) {
                results[index++]=activation.getResult();
            }
        }
    }

    int getWeight() {
        return weight;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getConverted() {
        return count;
    }

    @Override
    public void doAction(final MagicGame game,final MagicPlayer player) {
        for (final MagicSourceManaActivationResult result : results) {
            result.doActivation(game);
        }
    }

    public String getText() {
        final StringBuilder builder=new StringBuilder();
        builder.append('c').append(count);
        builder.append('-');
        builder.append('w').append(weight);
        builder.append('-');
        builder.append('x').append(x);
        for (final int amount : amountLeft) {
            builder.append('-').append(amount);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return x > 0 ? "X is " + x : "";
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this==obj) {
            return true;
        }
        if (obj==null||getClass()!=obj.getClass()) {
            return false;
        }
        final MagicBuilderPayManaCostResult other=(MagicBuilderPayManaCostResult)obj;
        return Arrays.equals(amountLeft,other.amountLeft);
    }

    @Override
    public int compareTo(final MagicBuilderPayManaCostResult result) {
        for (int index=0;index<MagicManaType.NR_OF_TYPES;index++) {
            final int dif=amountLeft[index]-result.amountLeft[index];
            if (dif!=0) {
                return dif;
            }
        }
        return 0;
    }

    @Override
    public long getId() {
        return hashCode;
    }

    @Override
    public long getStateId() {
        final long[] keys = new long[results.length + 4];
        keys[0] = weight;
        keys[1] = count;
        keys[2] = x;
        keys[3] = hashCode;
        int idx = 4;
        for (final MagicSourceManaActivationResult res : results) {
            keys[idx] = res.getStateId();
            idx++;
        }
        return MurmurHash3.hash(keys);
    }
}
