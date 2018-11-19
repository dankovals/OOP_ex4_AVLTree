/**
 * Represents a node in the tree data structure.
 * The node holds the information: father and sons, his value/data, and his height on the tree (0 for leafs).
 * All of the fields here are open for changing, with setters and getters
 * The Node represented by his value.
 * @author Danko
 */
class TreeNode {

    /** The value/data of this node.*/
    private int value;

    /** The height of this node, the distance from the deepest leaf in the tree that rooted from this node. */
    private int height;

    /** The left son of this node, null when its don't have left son.*/
    private TreeNode leftSon;

    /** The right son of this node, null when its don't have right son.*/
    private TreeNode rightSon;

    /** The father of this node, null when its the roof of the tree.*/
    private TreeNode father;

    /**
     * Constructor.
     * Initiate new node with the input value, and height 0.
     *
     * @param value The value of the node, this will represents the node
     */
    TreeNode(int value){
        this.value = value;
        this.leftSon = null;
        this.rightSon = null;
        this.father = null;
        this.height = 0;
    }

    /**
     * Sets new value for this node.
     *
     * @param newVal The new value of this node.
     */
    void setValue(int newVal){
        this.value = newVal;
    }

    /**@return the value of this node.*/
    int getValue(){
        return this.value;
    }

    /**
     * Adds some other node to be the left son of this node, automatically updates the father of new one.
     *
     * @param left The new left son of this node.
     */
    void setLeftSon(TreeNode left){
        this.leftSon = left;
        if (left != null) {
            left.setFather(this);
        }
    }

    /**@return the left son of this node.*/
    TreeNode getLeftSon(){
        return this.leftSon;
    }

    /**
     * Adds some other node to be the right son of this node, automatically updates the father of new one.
     *
     * @param right The new right son of this node.
     */
    void setRightSon(TreeNode right){
        this.rightSon = right;
        if (right != null) {
            right.setFather(this);
        }
    }

    /**@return the right son of this node.*/
    TreeNode getRightSon(){
        return this.rightSon;
    }

    /**
     * Adds some other node to be the father of this node
     *
     * @param father The new father of this node.
     */
    void setFather(TreeNode father){
        this.father = father;
    }

    /**@return the father of this node.*/
    TreeNode getFather(){
        return this.father;
    }

    /**
     * Sets the height of this node.
     *
     * @param height The new height of this node.
     */
    void setHeight(int height){
        this.height = height;
    }

    /**@return the left height of this node.*/
    int getHeight(){
        return this.height;
    }

    /**@return the balance of this node, i.e. the difference between the heights of his sons*/
    int getBalance(){
        int heightLeft;
        int heightRight;
        if (this.leftSon == null){
            heightLeft = -1;
        } else {
            heightLeft = this.leftSon.height;
        }
        if (this.rightSon == null){
            heightRight = -1;
        } else {
            heightRight = this.rightSon.height;
        }
        return heightLeft - heightRight;
    }

    /**@return A string representation of the node, represented by his value.*/
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
