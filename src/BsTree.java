import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a binary search tree data structure.
 * Its nodes holds only int values, and don't allowed any duplication of values
 * The tree holds some node for root, and by looking of all of his offspring we can get the whole tree.
 * The tree has the BST property, for each node, his left sub tree holds smaller values, and his
 * right subtree holds bigger values.
 * @author Danko
 */
class BsTree implements Iterable<Integer>, BinaryTree {

    /** The root of the BST, its holds the whole tree as his offspring. his father always null.*/
    TreeNode root;

    /** Holds the number of nodes in the tree.*/
    private int numOfElements;

    /** Holds a list of the tree values, in ascending order.*/
    ArrayList<Integer> inOrderArray;

    /** After deleting a node, its holds the father of the deleted node.*/
    TreeNode fatherOfLastDeleted;

    /**
     * constructor.
     * Initiate an empty BST.
     */
    public BsTree(){
        this.numOfElements = 0;
        this.root = null;
    }

    @Override
    public boolean add(int newValue) {
        if (contains(newValue) >= 0){
            return false;                   // When the value to add is already in the tree
        }
        numOfElements++;
        if (this.root == null) {            //When the tree is empty
            this.root = new TreeNode(newValue);
        } else {
            bstInsert(newValue, this.root);
        }
        return true;
    }

    /**
     * Its finds the relevant place to add the new value.
     * After adding, the tree still holds the BST property.
     * Its assumes that the new value is in the tree, we checked this before, with contains.
     *
     * @param newValue The value that gonna be added to the tree.
     * @param curRoot the root of the BST.
     */
    private void bstInsert(int newValue, TreeNode curRoot) {
        if (newValue < curRoot.getValue()) {
            if (curRoot.getLeftSon() == null) {                         // We find place to add left child
                curRoot.setLeftSon(new TreeNode(newValue));
            } else {                                                    // Search in the left subtree
                bstInsert(newValue, curRoot.getLeftSon());
            }
        } else {
            if (curRoot.getRightSon() == null) {                        // We find place to add right child
                curRoot.setRightSon(new TreeNode(newValue));
            } else {                                                    // Search in the right subtree
                bstInsert(newValue, curRoot.getRightSon());
            }
        }
    }

    @Override
    public boolean delete(int toDelete) {
        if (this.contains(toDelete) == -1) {                // The value not exist in the tree
            return false;
        }
        if (this.size() == 1){                            // The tree consist only the root
            this.root = null;
            this.fatherOfLastDeleted = null;
        } else {                            // When swap with successor, its returns the father of successor.
            this.fatherOfLastDeleted = bstRemove(toDelete, this.root);
        }
        numOfElements--;
        return true;
    }

    /**
     * Its finds the relevant node, and removes him according two three options.
     * After removing, the tree still holds the BST property.
     * Its assumes that the new value is in the tree, we checked this before, with contains
     *
     * @param valToDelete The value that gonna be removed from the tree.
     * @param root the root of the BST.
     * @return the father of the deleted node
     */
    private TreeNode bstRemove(int valToDelete, TreeNode root) {
        TreeNode nodeToDelete = getNodeFromValue(valToDelete, root);
        if ((nodeToDelete.getLeftSon() == null) && (nodeToDelete.getRightSon() == null)){
            return noSonsDeletion(valToDelete, nodeToDelete);
        } else if ((nodeToDelete.getLeftSon() != null) && (nodeToDelete.getRightSon() != null)) {
            return twoSonsDeletion(nodeToDelete);
        } else {
            return oneSonDeletion(valToDelete, nodeToDelete);
        }
    }

    /**
     * The node we want to delete is a leaf of the tree, its deleted him.
     *
     * @param value The value that gonna be removed from the tree.
     * @param nodeToDelete The node that gonna be remove.
     * @return the father of the deleted node
     */
    private TreeNode noSonsDeletion(int value, TreeNode nodeToDelete) {
        // Its not null, because we already checked for the option of only one node in the tree
        TreeNode parent = nodeToDelete.getFather();
        if (parent.getValue() > value) {
            parent.setLeftSon(null);
        } else {
            parent.setRightSon(null);
        }
        return parent;
    }

    /**
     * The node we want to delete has only one child, its deleted him, and connect his father to his child.
     *
     * @param value The value that gonna be removed from the tree.
     * @param nodeToDelete The node that gonna be remove.
     * @return the father of the deleted node
     */
    private TreeNode oneSonDeletion(int value, TreeNode nodeToDelete) {
        TreeNode parent = nodeToDelete.getFather();
        if (nodeToDelete.getLeftSon() != null){
            if (parent.getValue() > value) {
                parent.setLeftSon(nodeToDelete.getLeftSon());
            } else {
                parent.setRightSon(nodeToDelete.getLeftSon());
            }
        } else {
            if (parent.getValue() > value) {
                parent.setLeftSon(nodeToDelete.getRightSon());
            } else {
                parent.setRightSon(nodeToDelete.getRightSon());
            }
        }
        return parent;
    }

    /**
     * The node we want to delete both right and left sons, its swap him with his successor, (there is one
     * because its has right child). and deleted the successor with noSonDeletion or oneSonDeletion.
     *
     * @param nodeToDelete The node that gonna be remove.
     * @return the father of the successor.
     */
    private TreeNode twoSonsDeletion(TreeNode nodeToDelete) {
        TreeNode successor = findSuccessorRightSon(nodeToDelete);
        TreeNode parent;
        int successorVal = successor.getValue();        //
        if (successor.getRightSon() != null) {
            parent = oneSonDeletion(successorVal, successor);
        } else {
            parent = noSonsDeletion(successorVal, successor);        // Successor don't have left child
        }
        nodeToDelete.setValue(successorVal);
        return parent;
    }

    /**
     * Its finds the successor of node - the next value in ordered values of the tree.
     *
     * @param node The node that we look for his successor.
     * @return The successor, null if node is the nax value in the tree
     */
    private TreeNode getSuccessor(TreeNode node) {
        TreeNode curSuccessor = node.getRightSon();
        if (curSuccessor != null) {                             // Node holds right son.
            return findSuccessorRightSon(node);
        } else {                                                // Node don't have right child.
            while (node.getFather() != null){
                if (node.getFather().getValue() < node.getValue()) {
                    node = node.getFather();
                } else {                                        // We find successor!
                    return node.getFather();
                }
            }
            return null;            // Its arrives here only when we look for the successor of the max value.
        }
    }

    /**
     * Its finds the successor of node - the next value in ordered values of the tree, its assume that node
     * have right child.
     *
     * @param node The node that we look for his successor.
     * @return The successor, there is one, because of the assuming.
     */    private TreeNode findSuccessorRightSon(TreeNode node) {
        TreeNode curSuccessor = node.getRightSon();
        while (curSuccessor.getLeftSon() != null) {                 // Stops after visiting the successor.
            curSuccessor = curSuccessor.getLeftSon();
        }
        return curSuccessor;
    }

    /**
     * Sets the height of node. assume that his sons have correct heights.
     * If there is no son, it height will be regarded as minus one.
     *
     * @param node to set its height.
     */
    static void setNodeHeight(TreeNode node) {
        int heightLeft;
        int heightRight;
        if (node.getLeftSon() == null){                 // Don't have left son.
            heightLeft = -1;
        } else {
            heightLeft = node.getLeftSon().getHeight();
        }
        if (node.getRightSon() == null){                // Don't have right son.
            heightRight = -1;
        } else {
            heightRight = node.getRightSon().getHeight();
        }
        node.setHeight(Math.max(heightLeft, heightRight) + 1);
    }

    /**
     * Its find node according to his value. assume that the value in the tree.
     *
     * @param value to search for.
     * @param curRoot of the interesting sub tree.
     * @return the node that holds the required value.
     */
    static TreeNode getNodeFromValue(int value, TreeNode curRoot) {
        if (curRoot.getValue() == value){
            return curRoot;
        } else if (curRoot.getValue() > value){
            return getNodeFromValue(value, curRoot.getLeftSon());
        } else {
            return getNodeFromValue(value, curRoot.getRightSon());
        }
    }

    @Override
    public int contains(int searchVal) {
        int depth = -1;
        return getDepth(searchVal, root, depth);
    }

    /**
     * Its find the depth of the required node, assumes that the node is in the tree.
     *
     * @param searchVal- value to search for.
     * @param curRoot of the interesting sub tree.
     * @param curDepth- gets -1 for start, and goes up by one every iteration.
     * @return the depth of the node that holds the required value.
     */
    private int getDepth(int searchVal, TreeNode curRoot, int curDepth) {
        curDepth++;
        if (curRoot == null) {                     // Value not exist
            return -1;
        } else if (curRoot.getValue() < searchVal) {
            return getDepth(searchVal, curRoot.getRightSon(), curDepth);
        } else if (curRoot.getValue() > searchVal) {
            return getDepth(searchVal, curRoot.getLeftSon(), curDepth);
        } else {
            return curDepth;            // We find the value!
        }
    }

    @Override
    public int size() {
        return numOfElements;
    }

    /**
     * Sets the size of the data structures.
     *
     * @param newSize the new number of elements.
     */
    void setSize(int newSize) {
        numOfElements = newSize;
    }

    /**
     * Makes array from all the tree values, arranged in ascending order.
     *
     * @param curRoot the root of the tree.
     */
    void treeToArray(TreeNode curRoot) {
        if (curRoot == null) {
            return;
        }
        treeToArray(curRoot.getLeftSon());
        inOrderArray.add(curRoot.getValue());
        treeToArray(curRoot.getRightSon());
    }

    /**
     * @return an iterator that runs threw the values of the tree, in ascending order.
     */
    public Iterator<Integer> iterator() {
        return new inOrderIterator();
    }

    /**
     * A class that implements iterator for the BST.
     * The iterator holds only Integers, and returns the values of the tree in ascending order.
     */
    private class inOrderIterator implements Iterator<Integer> {

        /** The index of the current value in the tree.*/
        private int index;

        /**
         * constructor.
         * Initiate new list that holds the tree values arrange according to the natural order.
         */
        inOrderIterator() {
            inOrderArray = new ArrayList<>();
            treeToArray(root);
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public Integer next() {
            if (this.hasNext()) {
                Integer nextVal = inOrderArray.get(index);
                index++;
                return nextVal;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
