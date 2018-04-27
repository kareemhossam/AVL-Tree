/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avltree;

import java.io.*;
import java.util.*;

/**
 *
 * @author Kareem
 */
public class AVLtree {

    private Node root;
    private static int treeSize;

    private class Node {

        private String key;
        private int balance;
        private int height;
        private Node left, right, parent;

        Node(String k, Node p) {
            key = k;
            parent = p;
        }
    }

    public boolean insert(String key) {

        if (root == null) {
            root = new Node(key, null);
        } else {
            Node n = root;
            Node parent;
            while (true) {
                if (n.key.equalsIgnoreCase(key)) {
                    return false;
                }

                parent = n;

                //boolean goLeft = n.key > key;
                int goLeft = n.key.compareToIgnoreCase(key);

                n = (goLeft > 0) ? n.left : n.right;
                if (n == null) {
                    if (goLeft > 0) {
                        parent.left = new Node(key, parent);
                    } else {
                        parent.right = new Node(key, parent);
                    }
                    rebalance(parent);
                    break;
                }
            }
        }
        return true;
    }

    private void delete(Node node) {
        if (node.left == null && node.right == null) {
            if (node.parent == null) {
                root = null;
            } else {
                Node parent = node.parent;
                if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                rebalance(parent);
            }
            return;
        }
        if (node.left != null) {
            Node child = node.left;
            while (child.right != null) {
                child = child.right;
            }
            node.key = child.key;
            delete(child);
        } else {
            Node child = node.right;
            while (child.left != null) {
                child = child.left;
            }
            node.key = child.key;
            delete(child);
        }
    }

    public void delete(String delKey) {
        if (root == null) {
            return;
        }
        Node node = root;
        Node child = root;
        int comp;
        while (child != null) {
            node = child;
            comp = delKey.compareToIgnoreCase(node.key);
            child = comp >= 0 ? node.right : node.left;
            if (delKey.equalsIgnoreCase(node.key)) {
                delete(node);
                return;
            }
        }
    }

    private void rebalance(Node n) {
        setBalance(n);

        if (n.balance == -2) {
            if (height(n.left.left) >= height(n.left.right)) {
                n = rotateRight(n);
            } else {
                n = rotateLeftThenRight(n);
            }

        } else if (n.balance == 2) {
            if (height(n.right.right) >= height(n.right.left)) {
                n = rotateLeft(n);
            } else {
                n = rotateRightThenLeft(n);
            }
        }

        if (n.parent != null) {
            rebalance(n.parent);
        } else {
            root = n;
        }
    }

    private Node rotateLeft(Node a) {

        Node b = a.right;
        b.parent = a.parent;

        a.right = b.left;

        if (a.right != null) {
            a.right.parent = a;
        }

        b.left = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateRight(Node a) {

        Node b = a.left;
        b.parent = a.parent;

        a.left = b.right;

        if (a.left != null) {
            a.left.parent = a;
        }

        b.right = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }
        setBalance(a, b);
        return b;
    }

    private Node rotateLeftThenRight(Node n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Node rotateRightThenLeft(Node n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    private int height(Node n) {
        if (n == null) {
            return -1;
        }
        return n.height;
    }

    private void setBalance(Node... nodes) {
        for (Node n : nodes) {
            reheight(n);
            n.balance = height(n.right) - height(n.left);
        }
    }

    public void printBalance() {
        printBalance(root);
    }

    private void printBalance(Node n) {
        if (n != null) {
            printBalance(n.left);
            System.out.printf("%s ", n.balance);
            printBalance(n.right);
        }
    }

    public void printTree() {
        printTree(root);
    }

    private void printTree(Node n) {
        if (n != null) {
            printTree(n.left);
            System.out.printf("%s \n", n.key);
            treeSize++;
            printTree(n.right);
        }
        System.out.println("Dictionary Size: " + treeSize);
        treeSize = 0;
    }

    private void reheight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    //Modified Already
    public void find(String findKey) {
        if (root == null) {
            return;
        }
        Node node = root;
        Node child = root;
        int comp;
        while (child != null) {
            node = child;
            comp = findKey.compareToIgnoreCase(node.key);
            child = comp >= 0 ? node.right : node.left;
            if (findKey.equalsIgnoreCase(node.key)) {
                System.out.println("Found");
                return;
            }
        }
        System.out.println("not found");
    }

    //Modified already
    public void findBatch(String x) throws FileNotFoundException {
        File f = new File(x);
        Scanner s = new Scanner(f);

        while (s.hasNext()) {
            find(s.nextLine());
        }
    }

    //Modified already
    public void deleteBatch(String str) throws FileNotFoundException {
        File f = new File(str);
        Scanner s = new Scanner(f);

        while (s.hasNext()) {
            delete(s.nextLine());
        }
    }

    //new
    public void insertManually() {
        System.out.println("Enter the word");
        Scanner s = new Scanner(System.in);
        insert(s.nextLine());
    }

    //new
    public void insertBatch(String str) throws FileNotFoundException {
        File f = new File(str);
        System.out.println(str);
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            insert(s.nextLine());
        }
    }

    //new
    public void deleteManually() {
        System.out.println("Enter the word");
        Scanner s = new Scanner(System.in);
        delete(s.nextLine());
    }

    //new
    public void findManually() {
        System.out.println("Enter the word");
        Scanner s = new Scanner(System.in);
        find(s.nextLine());
    }

    public static void main(String[] args) throws FileNotFoundException {
        AVLtree tree = new AVLtree();
        boolean end = false;
        System.out.println("Welcome to Our Dictionary");
        System.out.println("Please enter a FileName/Directory to Start :");
        Scanner s = new Scanner(System.in);

        tree.insertBatch(s.nextLine());

        while (!end) {
            System.out.println("Please choose an option:");
            System.out.println("1- Insert more words");
            System.out.println("2- Print Current Dictionary and size");
            System.out.println("3- Delete words");
            System.out.println("4- Find Words");
            System.out.println("5- Exit");
            switch (Integer.parseInt(s.nextLine())) {
                case 1:
                    System.out.println("Press 1 to Insert one word Manually");
                    System.out.println("Press 2 to Insert from a File");
                    switch (Integer.parseInt(s.nextLine())) {
                        case 1:
                            tree.insertManually();
                            break;
                        case 2:
                            System.out.println("Insert File Name : ");
                            tree.insertBatch(s.nextLine());
                            break;
                        default:
                            System.out.println("Invalid Entry, please try again");
                            break;
                    }
                    break;
                case 2:
                    tree.printTree();

                    break;
                case 3:
                    System.out.println("Press 1 to Delete one word Manually");
                    System.out.println("Press 2 to Delete from a File");
                    switch (Integer.parseInt(s.nextLine())) {
                        case 1:
                            tree.deleteManually();
                            break;
                        case 2:
                            System.out.println("Insert File Name : ");
                            tree.deleteBatch(s.nextLine());
                            break;
                        default:
                            System.out.println("Invalid Entry, please try again");
                            break;
                    }
                    break;
                case 4:
                    System.out.println("Press 1 to Find one word Manually");
                    System.out.println("Press 2 to Find from a File");
                    switch (Integer.parseInt(s.nextLine())) {
                        case 1:
                            tree.findManually();
                            break;
                        case 2:
                            System.out.println("Insert File Name : ");
                            tree.findBatch(s.nextLine());
                            break;
                        default:
                            System.out.println("Invalid Entry, please try again");
                            break;
                    }
                    break;
                case 5:
                    end = true;
                    break;
                default:
                    System.out.println("Invalid Entry, please try again");
                    break;

            }
        }
    }
}
