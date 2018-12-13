package datastructures.dictionaries;

import java.lang.reflect.Array;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.datastructures.trees.BinarySearchTree.BSTNode;
// **new
/**
 * TODO: Replace this comment with your own as appropriate.
 *
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and callst o superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 *
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 *    instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 *    children array or left and right fields in AVLNode.  This will 
 *    instead mask the super-class fields (i.e., the resulting node 
 *    would actually have multiple copies of the node fields, with 
 *    code accessing one pair or the other depending on the type of 
 *    the references used to access the instance).  Such masking will 
 *    lead to highly perplexing and erroneous behavior. Instead, 
 *    continue using the existing BSTNode children array.
 * 4. If this class has redundant methods, your score will be heavily
 *    penalized.
 * 5. Cast children array to AVLNode whenever necessary in your
 *    AVLTree. This will result a lot of casts, so we recommend you make
 *    private methods that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 */
//test
public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V>  {
    // TODO: Implement me!
    public class AVLNode extends BSTNode {
        public int height;
        public AVLNode(K key, V value, int height) {
            super(key, value);
            this.height = height;
            this.children = (AVLNode[]) Array.newInstance(AVLNode.class, 2);
        }
    }
    
    private AVLNode newNode = null;
    
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        
        this.root = insert(key, value, caster(this.root));
        if (newNode != null) {
            // Make sure to make newNode null again!
            AVLNode returnNode = newNode;
            newNode = null;
            return returnNode.value;
        }
        return null;

    }
    
    
    private AVLNode insert(K key, V value, AVLNode node) {
        if (node == null) {
            size++;
            return new AVLNode(key, value, 0);
        }
        
        if (key.compareTo(node.key) < 0) {
            node.children[0] = insert(key, value, caster(node.children[0]));
        } else if (key.compareTo(node.key) > 0) {
            node.children[1] = insert(key, value, caster(node.children[1]));
        } else {
            newNode = node;
            AVLNode resultingNode = new AVLNode(key, value, node.height);
            resultingNode.children[0] = node.children[0];
            resultingNode.children[1] = node.children[1];
            return resultingNode;
        }
        
        if (correctHeightAndCheckUnbalanced(node)) {
            int cases = checkCase(node, key);
            if (cases == 1) {
                return leftRotation(node);
            } else if (cases == 2) {
                node.children[0] = rightRotation(caster(node.children[0]));
                return leftRotation(node);
            } else if (cases == 3) {
                node.children[1] = leftRotation(caster(node.children[1]));
                return rightRotation(node);
            } else {
                return rightRotation(node);
            }
        }
        return node;
    }
    //test comment
    private boolean correctHeightAndCheckUnbalanced(AVLNode node) {
        int leftHeight = (node.children[0] == null) ? -1:caster(node.children[0]).height;
        int rightHeight = (node.children[1] == null) ? -1:caster(node.children[1]).height;
        node.height =  Math.max(leftHeight, rightHeight) + 1;
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return true;
        }
        return false;
    }
    
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        AVLNode node = lookUp(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }
    
    private AVLNode lookUp(K key) {
        AVLNode prev = null;
        AVLNode current = caster(this.root);

        int child = -1;

        while (current != null) {
            int direction = Integer.signum(key.compareTo(current.key));
            if (direction == 0) {
                return current;
            }
            else {
                child = Integer.signum(direction + 1);
                current = caster(current.children[child]);
            }
        }
        return current;
    }

    public int checkCase(AVLNode root, K insertedKey) {
        if (root.key.compareTo(insertedKey) > 0) {
            if (((AVLNode)root.children[0]).key.compareTo(insertedKey) > 0) {
                return 1;
            } else {
                return 2;
            }
        } else {
            if (((AVLNode)root.children[1]).key.compareTo(insertedKey) > 0) {
                return 3;
            } else {
                return 4;
            }
        }        
    }
    
    public AVLNode rightRotation(AVLNode root) {
        AVLNode temp = (AVLNode) root.children[1];
        root.children[1] = (AVLNode) temp.children[0]; 
        temp.children[0] = root;
        root.height = 1 + Math.max(getHeight(caster(root), 0), getHeight(caster(root), 1));
        temp.height = 1 + Math.max(getHeight(caster(temp), 0), getHeight(caster(temp), 1));
        root = temp;
        return root;
    }
    
    private int getHeight(AVLNode node, int direction) {
        return (caster(node.children[direction / 1]) == null) ? -1 : caster(node.children[direction / 1]).height;
    }
    
    public AVLNode leftRotation(AVLNode root) {
        AVLNode temp = (AVLNode) root.children[0];

        root.children[0] = (AVLNode) temp.children[1];
        temp.children[1] = root;
        
        root.height = Math.max(getHeight(caster(root),0), getHeight(caster(root),1)) + 1;
        temp.height = Math.max(getHeight(caster(temp),0), getHeight(caster(temp),1)) + 1;
        
        root = temp;
        return root;
    }
    
    public AVLNode caster(BSTNode node) {
        return (AVLNode) node;
    }    
    
}
