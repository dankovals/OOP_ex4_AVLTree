

public class TEST{

    public TEST() {
        System.out.println("HELLO");
        AvlTree avlTree = new AvlTree();
        avlTree.add(3);
        avlTree.add(5);
        System.out.println(avlTree.contains(3));
        System.out.println(avlTree.contains(5));
        System.out.println(avlTree.contains(6));
    }

    public static void main(String[] args) {
        TEST t = new TEST();
    }

}
