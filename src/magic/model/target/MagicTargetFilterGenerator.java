package magic.model.target;

/**
 * Generator for parametric filters with one or more numeric parameters.
 * Each '#' in the text is matched with one number from input.
 * Number of parameters will always be equal to the number of '#' characters in the text.
 */
public interface MagicTargetFilterGenerator<T extends MagicTarget> {
    MagicTargetFilter<T> generate(Integer... parameters);
}
