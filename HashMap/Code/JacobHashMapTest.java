public class JacobHashMapTest {
    public static void main(String[] args) {
        JacobHashMap<String> map = new JacobHashMap<>();

        map.add("apple");
        map.add("banana");
        map.add("cherry");
        map.add("date");
        map.add("elderberry");
        map.add("fig");
        map.add("grape");
        map.add("honeydew");

        System.out.println("Contains 'banana'? " + map.contains("banana"));
        System.out.println("Contains 'kiwi'?   " + map.contains("kiwi"));
        System.out.println("Initial capacity:  " + map.capacity());

        map.resize(32);
        System.out.println("Capacity after manual resize: " + map.capacity());
    }
}
