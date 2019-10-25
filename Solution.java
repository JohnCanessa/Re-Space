import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * Node for Tries.
 */
class TrieNode {

	// ***** members ****
	char data;
	public HashMap<Character, TrieNode> children; // could use a LinkedList or array
	TrieNode parent;
	public boolean isEnd;

	// **** constructor ****
	public TrieNode(char c) {
		this.data = c;
		this.children = new HashMap<Character, TrieNode>();
		this.isEnd = false;
	}

	// **** constructor ****
	public TrieNode() {
		this.children = new HashMap<Character, TrieNode>();
	}

	// **** get the child node associated with this character ****
	public TrieNode getChild(char ch) {

		// // ???? ????
		// System.out.println("getChild <<< ch: " + ch);

		// **** ****
		if (children != null) {
			return children.get(ch);
		}

		// **** ****
		return null;
	}

	// **** ****
	protected List<String> getWords() {

		// **** ****
		List<String> list = new ArrayList<String>();

		// **** add this word to the list ****
		if (isEnd) {

			// // ???? ????
			// System.out.println("getWords <<< isEnd: " + isEnd);

			list.add(toString());

			// // ???? ????
			// System.out.println("getWords <<< list: " + list.toString());
		}

		// **** ****
		if (children != null) {

			// **** iterate through the children ****
			Iterator<Entry<Character, TrieNode>> it = children.entrySet().iterator();
			while (it.hasNext()) {

				// **** ****
				Entry<Character, TrieNode> pair = (Map.Entry<Character, TrieNode>) it.next();

				// // ???? ????
				// System.out.println("getWords <<< ch: " + pair.getKey());

				// **** ****
				TrieNode child = (TrieNode) pair.getValue();
				list.addAll(child.getWords());

				// // ???? ????
				// System.out.println("getWords <<< list: " + list.toString());
			}
		}

		// **** ****
		return list;
	}

	// **** ****
	public String toString() {
		if (parent == null) {
			return "";
		} else {
			return parent.toString() + new String(new char[] { data });
		}
	}
}

/*
 * Trie class implementation.
 */
class Trie {

	// **** trie root ****
	private TrieNode root;

	// **** constructor ****
	public Trie() {
		root = new TrieNode('*');
	}

	// **** constructor using an array list ****
	public Trie(ArrayList<String> list) {

		// **** ****
		root = new TrieNode();

		// **** ****
		for (String word : list)
			insert(word);
	}

	// **** constructor using an array of strings ****
	public Trie(String[] arr) {

		// **** ****
		root = new TrieNode();

		// **** ****
		for (String word : arr)
			insert(word);
	}

	// **** insert string into trie ****
	public void insert(String str) {

		// **** ****
		if (search(str) == true) {

			// // ???? ????
			// System.out.println("insert <<< str ==>" + str + "<== FOUND");

			// **** nothing else to do ****
			return;
		}

		// ???? ????
		System.out.println("insert <<< str ==>" + str + "<==");

		// **** ****
		TrieNode current = root;
		TrieNode pre;

		// **** ****
		for (char ch : str.toCharArray()) {

			// // ???? ????
			// System.out.println("insert <<< ch: " + ch);

			// **** ****
			pre = current;
			TrieNode child = current.getChild(ch);

			// **** ****
			if (child != null) {

				// // ???? ????
				// System.out.println("insert <<< child != null");

				current = child;
				child.parent = pre;
			} else {

				// // ???? ????
				// System.out.println("insert <<< child == null");

				current.children.put(ch, new TrieNode(ch));
				current = current.getChild(ch);
				current.parent = pre;
			}
		}

		// **** ****
		current.isEnd = true;
	}

	// **** search for a complete word in the trie ****
	public boolean search(String str) {

		// **** start at the root of the trie ****
		TrieNode current = root;

		// **** look for the string ****
		for (char ch : str.toCharArray()) {
			if (current.getChild(ch) == null)
				return false;
			else
				current = current.getChild(ch);
		}

		// **** check if the string is a full word ****
		if (current.isEnd == true)
			return true;
		else
			return false;
	}

	// **** auto complete ****
	public List<String> autoComplete(String prefix) {

		// ???? ????
		System.out.println("autoComplete <<< prefix ==>" + prefix + "<==");

		// **** ****
		TrieNode lastNode = root;

		// **** ****
		for (int i = 0; i < prefix.length(); i++) {

			// // ???? ????
			// System.out.println("autoComplete <<< prefix.charAt(" + i + "): " +
			// prefix.charAt(i));

			// **** ****
			lastNode = lastNode.getChild(prefix.charAt(i));
			if (lastNode == null)
				return new ArrayList<String>();
		}

		// **** ****
		return lastNode.getWords();
	}

	// **** determine if trie contains this prefix ****
	public boolean contains(String prefix, boolean exact) {

		// **** start with the root of the ****
		TrieNode lastNode = getRoot();

		// **** ****
		for (int i = 0; i < prefix.length(); i++) {

			// **** ****
			lastNode = lastNode.getChild(prefix.charAt(i));

			// **** ****
			if (lastNode == null)
				return false;
		}

		// **** ****
		return !exact || lastNode.isEnd;
	}

	// **** determine if this trie contains this prefix ****
	public boolean contains(String prefix) {
		return contains(prefix, false);
	}

	// **** return the root node of the trie ****
	public TrieNode getRoot() {
		return root;
	}

	// **** add this string to the trie ****
	public void add(String str) {
		add(getRoot(), str);
	}

	// **** add this string to trie recursively ****
	private void add(TrieNode root, String str) {

		// **** check for end condition ****
		if (str.length() == 0)
			return;

		// **** get the next character from this string****
		char ch = str.charAt(0);

		// **** get the child associated with this character ****
		TrieNode child = root.getChild(ch);

		// **** add new trie node (if needed) ****
		if (child == null) {
			TrieNode prev = root;
			child = new TrieNode(ch);
			child.parent = prev;
			root.children.put(ch, child);
		}

		// **** check if this is the last character in the string ****
		if (str.length() == 1)
			child.isEnd = true;
		else
			add(child, str.substring(1));
	}
}

/*
 * Solution and test scaffolding.
 */
public class Solution {

	// **** recover mangled string ****
	static String recover(String mang, Trie dict) {

		// **** ****
		StringBuilder sb = new StringBuilder();

		StringBuilder badSB = new StringBuilder();

		// ???? ????
		System.out.println("recover <<< mang ==>" + mang + "<==");

		// **** traverse the mangled string ****
		String str = "";
		for (int i = 0; i < mang.length(); i++) {

			// **** append next character to string ****
			str += mang.substring(i, i + 1);

			// **** look up this string in the dictionary ****
			boolean found = dict.contains(str, false);

			// **** check if this string was NOT found ****
			if (!found) {

				// **** append character(s) to bad string ****
				badSB.append(str);

				// **** clear string ****
				str = "";
			} else {

				// **** check if this is a complete word ****
				boolean complete = dict.contains(str, true);

				// **** check if complete word in dictionary ****
				if (complete) {

					// **** convert to upper case ****
					String upperCase = badSB.toString().toUpperCase();

					// **** append upper case string with or without leading space ****
					if (upperCase.length() != 0) {
						if (sb.length() != 0)
							sb.append(" ");
						sb.append(upperCase);
					}

					// **** clear the string ****
					badSB.setLength(0);

					// **** append string to string builder with or without leading space ****
					if (sb.length() != 0)
						sb.append(" ");
					sb.append(str);

					// **** clear string ****
					str = "";
				}
			}
		}

		// **** append missing bad word (if needed) ****
		if (badSB.length() != 0) {

			// **** append a space (if needed) ****
			if (sb.length() != 0)
				sb.append(" ");

			// **** append the bad word ****
			sb.append(badSB.toString().toUpperCase());
		}

		// **** return the unmangled string ****
		return sb.toString();
	}

	// **** ****
	public static void main(String[] args) {

		// // **** build array list to populate trie ****
		// ArrayList<String> al = new ArrayList<String>();
		// al.add("amazon");
		// al.add("amazon prime");
		// al.add("amazing");
		// al.add("amazing spider man");
		// al.add("amazed");
		//
		// al.add("alibaba");
		// al.add("ali express");
		// al.add("ebay");
		// al.add("walmart");
		//
		// // **** instantiate new trie ****
		// Trie t = new Trie(al);

		// // **** build array of strings to populate trie ****
		// String[] arr = {"amazon", "amazon prime", "amazing", "amazing spider man",
		// "amazed",
		// "alibaba", "ali express", "ebay", "walmart"};
		//
		// // **** instantiate new trie ****
		// Trie t = new Trie(arr);

		// **** instantiate new trie ****
		Trie t = new Trie();

		// **** add strings to the trie ****
		t.insert("amazon");
		t.insert("amazon prime");
		t.insert("amazing");
		t.insert("amazing spider man");
		t.insert("amazed");

		t.insert("soda");
		t.insert("cake");
		t.insert("soda");
		t.insert("soda pop");

		// **** add more string(s) to the trie recursively ****
		t.add("coca cola");
		t.add("coconut");
		t.add("cacao");
		t.add("cat");
		t.add("catnip");

		// ???? ????
		System.out.println();

		// **** auto complete using this prefix ****
		// List<String> a = t.autoComplete("amaz");
		// List<String> a = t.autoComplete("wall");
		List<String> a = t.autoComplete("c");

		// ???? ????
		System.out.println("\nmain <<< a.size: " + a.size());

		// **** display auto complete strings ****
		for (int i = 0; i < a.size(); i++)
			System.out.println("main <<< a[" + i + "]: " + a.get(i));
		System.out.println();

		// **** trie contains specified prefix ****
		System.out.println("main <<<       contains(\"a\", true): " + t.contains("a", true));
		System.out.println("main <<<      contains(\"a\", false): " + t.contains("a", false) + "\n");

		System.out.println("main <<<    contains(\"amaz\", true): " + t.contains("amaz", true));
		System.out.println("main <<<   contains(\"amaz\", false): " + t.contains("amaz", false) + "\n");

		System.out.println("main <<<          contains(\"amaz\"): " + t.contains("amaz"));
		System.out.println("main <<<          contains(\"amaz\"): " + t.contains("amaz") + "\n");

		System.out.println("main <<<  contains(\"amazon\", true): " + t.contains("amazon", true));
		System.out.println("main <<< contains(\"amazon\", false): " + t.contains("amazon", false) + "\n");

		System.out.println("main <<<        contains(\"amazon\"): " + t.contains("amazon"));
		System.out.println("main <<<        contains(\"amazon\"): " + t.contains("amazon") + "\n");

		System.out.println("main <<<           contains(\"abc\"): " + t.contains("abc"));
		System.out.println("main <<<           contains(\"abc\"): " + t.contains("abc") + "\n");

		// **** mangled string ****
		String mangled = "jesslookedjustliketimherbrother";
		// String mangled = "thisismikesfavoritefood";
		// String mangled = "thisisawesome";
		// String mangled = "doyouliketodrinkcoke";

		// **** words for our dictionary (implemented as a trie) ****
		String[] words = { "just", "this", "favorite", "awesome", "like", "brother", "looked", "food", "is", "her", "to",
				"drink", "do", "you", "soda" };

		// **** instantiate and populate dictionary (a trie) ****
		Trie dict = new Trie(words);
		System.out.println();

		// **** recover the string ****
		String unmangled = recover(mangled, dict);
		System.out.println("main <<< unmangled ==>" + unmangled + "<==");
	}

}
