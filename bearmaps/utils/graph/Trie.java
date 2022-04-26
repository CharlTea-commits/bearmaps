package bearmaps.utils.graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie(List<String> stringy) {
        root = new TrieNode('?', new HashMap<Character, TrieNode>(), false);
        for (String s : stringy) {
            insert(s);
        }
    }

    private void insert(String s) {
        TrieNode curr = root;
        for (int i = 0, n = s.length(); i < n; i++) {
            Character c = s.charAt(i);
            if (!(curr.myBabies.containsKey(c))) {
                curr.myBabies.put(c, new TrieNode(c, new HashMap<Character, TrieNode>(), false));
            }
            curr = curr.myBabies.get(c);
        }
        curr.wordEnd = true;
    }

    private List <String> prefixHelper(TrieNode curr, List <String> keys, String prefix) {
        if (curr.wordEnd) {
            keys.add(prefix);
        }
        for (Character c : curr.myBabies.keySet()) {
            TrieNode kid = curr.myBabies.get(c);
            String thisWord = prefix + kid.item;
            prefixHelper(kid, keys, thisWord);
        }
        return keys;
    }

    public List<String> keysWithPrefix(String prefix) {
        List <String> daKeys = new ArrayList<String>();
        TrieNode curr = root;
        String meep = "";
        for (int i= 0, n = prefix.length() ; i <n; i++) {
            Character c = prefix.charAt(i);
            meep += c;
            if (!(curr.myBabies.containsKey(c))) {
                return daKeys;
            }
            curr = curr.myBabies.get(c);

        }
        return prefixHelper(curr, daKeys, meep);
    }



    public class TrieNode{
        Character item;
        HashMap<Character, TrieNode> myBabies;
        boolean wordEnd;

        public TrieNode(Character c, HashMap<Character, TrieNode> l, boolean b) {
            item = c;
            myBabies = l;
            wordEnd = b;

        }
    }
}
