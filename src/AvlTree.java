import java.util.ArrayList;

/**
 * Represents an AVL tree data structure.
 * It has the same properties like the BST, but its always stay balance. In addition and deletion,
 * the avl do the same like the bst, and after this, its looking for violation, according to the heights of
 * his sons. And if there is any violations, its repair them with the rotation operations.
 * @author Danko
 */
public class AvlTree extends BsTree{

    /** Constant, information for printing, in situation that the user try build avl with null.*/
    private static final String CONST_GOT_NULL_ERROR =
                                    "ERROR: Could not initiate new Avl! The constructor's input was - null";

    /**
     * The default constructor.
     * Initiate an empty AVL tree.
     */
    public AvlTree(){
        super();
    }
    
    /**
     * A constructor that builds a new AVL tree containing all unique values in the input array.
     *
     * @param data the values to add to tree.
     */
    public AvlTree(int[] data) {
        super();
        try {
            for (int value : data) {
                add(value);
            }
        } catch (NullPointerException e) {
            System.out.println(CONST_GOT_NULL_ERROR);
        }
    }

    /**
     * A copy constructor that creates a deep copy of the given oop.ex4.data_structures.AvlTree. The new tree
     * contains all the values of the given tree, but not necessarily in the same structure.
     *
     * @param avlTree an AVL tree.
     */
    public AvlTree(BsTree avlTree) {
        super();
        try {
            avlTree.inOrderArray = new ArrayList<>();
            avlTree.treeToArray(avlTree.root);
            this.root = avlFromSorted(avlTree.inOrderArray, 0, avlTree.size() - 1);
            setSize(avlTree.size());
        }catch (NullPointerException e) {
            e.getCause();
            System.out.println(CONST_GOT_NULL_ERROR);
        }
    }

    /**
     * Builds an avl tree from a sorted array. it happens in O(n), when n is the size of the array.
     *
     * @param array - must be a sorted array, without duplicate values.
     * @param start - the first index in the array to insert to the new avl.
     * @param end - the last index in the array to insert to the new avl.
     * @return the root of the new balanced avl tree.
     */
    private TreeNode avlFromSorted(ArrayList<Integer> array, int start, int end) {
        if (start > end) {
            return null;
        }
        int curMid = start + ((end - start) / 2);
        TreeNode node = new TreeNode(array.get(curMid));
        node.setLeftSon(avlFromSorted(array, start, curMid - 1));
        node.setRightSon(avlFromSorted(array, curMid + 1, end));
        return node;
    }

    @Override
    public boolean add(int newValue) {
        if (! super.add(newValue)){             // Adds the value according to the bst property, if possible.
            return false;
        }
        TreeNode newNode = getNodeFromValue(newValue, this.root);
        TreeNode curNode = newNode.getFather();     // Next lines checks for violations,
        while (curNode != null){    // its stop after visiting the root, or after finding the first violation
            if (Math.abs(curNode.getBalance()) <= 1) {                      // curNode is balance
                setNodeHeight(curNode);
                curNode = curNode.getFather();
            } else {                                                        // We have violation
                fixViolation(curNode);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean delete(int toDelete) {
        if (! super.delete(toDelete)){         // Delete the value according to the bst property, if possible.
            return false;
        }
        while (fatherOfLastDeleted != null) {                   // Stops only after visiting in the root.
            if (Math.abs(fatherOfLastDeleted.getBalance()) <= 1){           // curNode i balance
                setNodeHeight(fatherOfLastDeleted);
                fatherOfLastDeleted = fatherOfLastDeleted.getFather();
            } else {                                                        // We have some violation
                fixViolation(fatherOfLastDeleted);
            }
        }
        return true;
    }

    /**
     * Its check which kind of violation we have here, and send to the necessary rotations.
     * after the rotation the sub tree that rooted from badNode will be balance, and all the
     * heights will be updated.
     *
     * @param badNode The Node that violate the balancing of the tree.
     */
    private void fixViolation(TreeNode badNode) {
        String violationKind = getViolationKind(badNode);
        switch (violationKind){
            case "LL":
                rotateRight(badNode);
                break;
            case "RR":
                rotateLeft(badNode);
                break;
            case "LR":
                rotateLeft(badNode.getLeftSon());
                setNodeHeight(badNode.getLeftSon());
                rotateRight(badNode);
                break;
            case "RL":
                rotateRight(badNode.getRightSon());
                setNodeHeight(badNode.getRightSon());
                rotateLeft(badNode);
                break;
        }
    }

    /**
     * According to the violating node, its understands which kind of rotations will fix the balancing
     * of the bad node.
     *
     * @param badNode The Node that violate the balancing of the tree.
     * @return A string represents the kind of violation.
     */
    private String getViolationKind(TreeNode badNode) {
        if (badNode.getBalance() > 1){                 // violation from left sub tree
            if (badNode.getLeftSon().getBalance() >= 0)
                return "LL";
            else {
                return "LR";
            }
        } else {                                        // violation from right sub tree
            if (badNode.getRightSon().getBalance() <= 0){
                return "RR";
            } else {
                return "RL";
            }
        }
    }

    /**
     * Its makes rotation right. Left son of node, will be the new root, node will be right son of new root,
     * and the sub tree that rooted from his right son, will be the left son of the node.
     * After changing order, node will be the only one to change is height, so its send for repairing.
     *
     * @param node The Node that we want to active the rotation on.
     */
    private void rotateRight(TreeNode node) {
        TreeNode newRoot = node.getLeftSon();
        TreeNode rightSubTree = newRoot.getRightSon();
        TreeNode upper = node.getFather();
        node.setLeftSon(rightSubTree);
        newRoot.setRightSon(node);
        connectUpper(upper, newRoot);
        setNodeHeight(node);
    }

    /**
     * Its makes rotation left. Right son of node, will be the new root, node will be left son of new root,
     * and the sub tree that rooted from his left son, will be the right son of the node.
     * After changing order, node will be the only one to change is height, so its send for repairing.
     *
     * @param node The Node that we want to active the rotation on.
     */
    private void rotateLeft(TreeNode node) {
        TreeNode newRoot = node.getRightSon();
        TreeNode leftSubTree = newRoot.getLeftSon();
        TreeNode upper = node.getFather();
        node.setRightSon(leftSubTree);
        newRoot.setLeftSon(node);
        connectUpper(upper, newRoot);
        setNodeHeight(node);
    }

    /**
     * After making the rotation in the sub tree of the bad node, this func connect the new root to the
     * other part of the tree.
     *
     * @param upper the father of the bad node.
     * @param newRoot the substitutes of bad node, needs to the son of upper.
     */
    private void connectUpper(TreeNode upper, TreeNode newRoot) {
        if (upper != null){                                 // The violation not in the root
            if (upper.getValue() > newRoot.getValue()){
                upper.setLeftSon(newRoot);
            } else {
                upper.setRightSon(newRoot);
            }
        } else {                                            // The violation in the root
            root = newRoot;
            newRoot.setFather(null);
        }
    }

    /**
     * Calculates the minimum number of nodes in an AVL tree of height h.
     *
     * @param h the height of the tree (a non negative number) in question.
     * @return the minimum number of nodes in an AVL tree of the given height.
     */
    public static int findMinNodes(int h) {
        int curMin = 1;
        if (h == 1) {
            curMin = 2;
        } else if (h > 1) {
            int twoSmaller = 1;
            int oneSmaller = 2;
            for (int curH = 2; curH <= h; curH++) {
                curMin = twoSmaller + oneSmaller + 1;
                twoSmaller = oneSmaller;
                oneSmaller = curMin;
            }
        }
        return curMin;
    }
}