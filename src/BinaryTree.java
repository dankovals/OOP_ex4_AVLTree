/**
 * The methods of this interface, are common for all the binary trees.
 * @author Danko
 */
public interface BinaryTree {

    /**
     * Add a new node with the given key to the tree.
     *
     * @param newValue the value of the new node to add.
     * @return true if the value to add is not already in the tree and it was successfully added,
     * false otherwise.
     */
    boolean add(int newValue);

    /**
     * Removes the node with the given value from the tree, if it exists.
     *
     * @param toDelete the value to remove from the tree.
     * @return true if the given value was found and deleted, false otherwise.
     */
    boolean delete(int toDelete);

    /**
     * Check whether the tree contains the given input value.
     *
     * @param searchVal the value to search for.
     * @return the depth of the node (0 for the root) with the given value if it was found in the tree,
     * -1 otherwise.
     */
    int contains(int searchVal);

    /**
     * @return the number of nodes in the tree.
     */
    int size();
}
