import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL Tree.
 *
 * @author KATE JEONG
 * @userid kjeong40
 * @GTID 903886263
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it appears in the Collection.
     *
     * @throws IllegalArgumentException if data or any element in data is null
     * @param data the data to add to the tree
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        for (T dataElement : data) {
            if (dataElement == null) {
                throw new IllegalArgumentException("Data is null.");
            }
            add(dataElement);
        }
    }

    /**
     * Adds the data to the AVL. Start by adding it as a leaf like in a regular
     * BST and then rotate the tree as needed.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors going up the tree,
     * rebalancing if necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        root = addHelper(data, root);
    }

    /** Recursive helper for add method
     *
     * @param data data to be added in
     * @param node node to add into
     * @return node
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> node) {
        if (node == null) {
            size++;
            return new AVLNode<T>(data);
        }
        if (data.compareTo(node.getData()) < 0) {
            node.setLeft(addHelper(data, node.getLeft()));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(addHelper(data, node.getRight()));
        }
        updateHeight(node);
        updateBF(node);
        return rotate(node);
    }

    /** Performs left rotation
     *
     * @param node note to rotate on
     * @return rightChild
     */
    private AVLNode<T> leftRotation(AVLNode<T> node) {
        AVLNode<T> rightChild = node.getRight();
        AVLNode<T> leftChild = rightChild.getLeft();
        rightChild.setLeft(node);
        node.setRight(leftChild);
        updateHeight(node);
        updateBF(node);
        updateHeight(rightChild);
        updateBF(rightChild);
        return rightChild;
    }

    /** Checks whether rotation is needed and if yes, performs corresponding rotation
     *
     * @param node to check rotation
     * @return node
     */
    private AVLNode<T> rotate(AVLNode<T> node) {
        if (node.getBalanceFactor() < -1) {
            AVLNode<T> right = node.getRight();
            if (right.getBalanceFactor() <= 0) {
                return leftRotation(node);
            } else {
                node.setRight(rightRotation(node.getRight()));
                return leftRotation(node);
            }
        } else if (node.getBalanceFactor() > 1) {
            AVLNode<T> left = node.getLeft();
            if (left.getBalanceFactor() >= 0) {
                return rightRotation(node);
            } else {
                node.setLeft(leftRotation(node.getLeft()));
                return rightRotation(node);
            }
        }
        return node;
    }

    /** Updates balance factor of nodes
     *
     * @param node to update BF on
     * @return node
     */
    private AVLNode<T> updateBF(AVLNode<T> node) {
        AVLNode<T> left = node.getLeft();
        AVLNode<T> right = node.getRight();
        int leftHeight = -1;
        int rightHeight = -1;
        if (left != null) {
            leftHeight = left.getHeight();
        }
        if (right != null) {
            rightHeight = right.getHeight();
        }
        int balanceFactor = leftHeight - rightHeight;
        node.setBalanceFactor(balanceFactor);
        return node;
    }

    /** Updates height of nodes
     *
     * @param node to update height of
     */
    private void updateHeight(AVLNode<T> node) {
        AVLNode<T> left = node.getLeft();
        AVLNode<T> right = node.getRight();
        int leftHeight = -1;
        int rightHeight = -1;
        if (left != null) {
            leftHeight = left.getHeight();
        }
        if (right != null) {
            rightHeight = right.getHeight();
        }
        int height = Math.max(leftHeight, rightHeight) + 1;
        node.setHeight(height);
    }

    /** Performs right rotation
     *
     * @param node note to rotate on
     * @return leftChild
     */
    private AVLNode<T> rightRotation(AVLNode<T> node) {
        AVLNode<T> leftChild = node.getLeft();
        AVLNode<T> rightChild = leftChild.getRight();
        leftChild.setRight(node);
        node.setLeft(rightChild);
        updateHeight(node);
        updateBF(node);
        updateHeight(leftChild);
        updateBF(leftChild);
        return leftChild;
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the predecessor to replace the data,
     * not the successor. As a reminder, rotations can occur after removing
     * the predecessor node.
     *
     * Remember to recalculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        AVLNode<T> removedNode = new AVLNode<>(data);
        root = removeHelper(data, root, removedNode);
        return removedNode.getData();
    }

    /** Recursive helper for remove method
     *
     * @param data data to remove
     * @param node current node
     * @param removedNode dummy node that holds node to remove
     * @return node to remove
     */
    private AVLNode<T> removeHelper(T data, AVLNode<T> node, AVLNode<T> removedNode) {
        if (node == null) {
            throw new NoSuchElementException("Data is not in tree.");
        }
        if (node.getData().compareTo(data) < 0) {
            node.setRight(removeHelper(data, node.getRight(), removedNode));
        } else if (node.getData().compareTo(data) > 0) {
            node.setLeft(removeHelper(data, node.getLeft(), removedNode));
        } else {
            removedNode.setData(node.getData());
            size--;
            // 3 removal methods
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            if (node.getLeft() == null) {
                return node.getRight();
            }
            if (node.getRight() == null) {
                return node.getLeft();
            }
            if (node.getRight() != null && node.getLeft() != null) {
                // next smallest ; one right and then as left as possible
                AVLNode<T> replacedNode = new AVLNode<>(null);
                node.setLeft(removeSecondHelper(data, node.getLeft(), replacedNode));
                node.setData(replacedNode.getData());
            }
        }
        updateHeight(node);
        updateBF(node);
        return rotate(node);
    }
    /** Recursive helper for removing predecessor
     *
     * @param data data to be removed
     * @param node current node
     * @param replacedNode dummy node
     * @return node
     */
    private AVLNode<T> removeSecondHelper(T data, AVLNode<T> node, AVLNode<T> replacedNode) {
        if (node.getRight() == null) {
            replacedNode.setData(node.getData());
            return node.getLeft();
        }
        node.setRight(removeSecondHelper(data, node.getRight(), replacedNode));
        return node;
    }
    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        T result = containsHelper(data, root);
        if (result == null) {
            throw new NoSuchElementException("Data is not in tree.");
        }
        return result;
    }

    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        return containsHelper(data, root) != null;
    }

    /** Recursive helper for contains method
     *
     * @param data data we are looking for
     * @param node current node
     * @return node's data
     */
    private T containsHelper(T data, AVLNode<T> node) {
        if (node == null) {
            return null;
        }
        if (data.compareTo(node.getData()) == 0) {
            return node.getData();
        } else if (data.compareTo(node.getData()) < 0) {
            return containsHelper(data, node.getLeft());
        } else {
            return containsHelper(data, node.getRight());
        }
    }
    /**
     * Finds and retrieves the median data of the passed in AVL. 
     * 
     * This can be done in O(n) time. That being said, this method will not need
     * to traverse the entire tree to function properly, so you should only
     * traverse enough branches of the tree necessary to find the median data
     * and only do so once. Failure to do so will result in an efficiency penalty.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *              50
     *            /    \
     *         25      75
     *        /  \     / \
     *      13   37  70  80
     *    /  \    \      \
     *   12  15    40    85
     * 
     * findMedian() should return 40
     *
     * @throws java.util.NoSuchElementException if the tree is empty or contains an even number of data
     * @return the median data of the AVL
     */
    public T findMedian() {
        // mod to find if size % 2 == 0; if even, throw exception
        // find bf of root and see which child is heavier
        // go to heavier child and pre-order traversal
        // increment counter till reach (size / 2 + 1)
        // return median
        if (root == null || size % 2 == 0) {
            throw new NoSuchElementException("Tree is empty or contains even number of data.");
        }
        AVLNode<Integer> count = new AVLNode<Integer>(0);
        AVLNode<T> answer = new AVLNode<>(null);
        inorderHelper(root, count, answer);
        return answer.getData();
    }

    /** Recursive helper method for findMedian method to traverse through tree till find median

     *
     * @param node the node passed in to start traversing at
     * @param count counter that will let us know when we find median
     * @param answer the median we are finding
     */
    private void inorderHelper(AVLNode<T> node, AVLNode<Integer> count, AVLNode<T> answer) {
        int median = size / 2 + 1;
        if (node == null) {
            return;
        }
        if (count.getData() != median) {
            inorderHelper(node.getLeft(), count, answer);
        }
        count.setData(count.getData() + 1);
        if (count.getData() == median) {
            answer.setData(node.getData());
            return;
        }
        if (count.getData() < median) {
            inorderHelper(node.getRight(), count, answer);
        }
    }
    /**
     * Clears the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Since this is an AVL, this method does not need to traverse the tree
     * and should be O(1)
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    /**
     * Returns the size of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the AVL tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }

    /**
     * Returns the root of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the AVL tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}