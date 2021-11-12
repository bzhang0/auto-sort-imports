import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static class Data {

        String left;
        List<String> contents;
        String path;

        public Data(String left, String path) {
            this.left = left;
            this.contents = new ArrayList<>();
            this.path = path;
        }

        public void add (String value) {
            this.contents.add(value);
        }

        public void sort () {
            Collections.sort(this.contents, new AsComparator());
        }
    }

    public static class AsComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            int s1as = s1.indexOf(" as ");
            int s2as = s2.indexOf(" as ");

//            System.out.println(s1 + " " + s1as);
//            System.out.println(s2 + " " + s2as);

            String temp1 = (s1as != -1) ? s1.substring(s1as + 4) : s1;
            String temp2 = (s2as != -1) ? s2.substring(s2as + 4) : s2;

//            System.out.println(temp1 + " " + temp2);
//            System.out.println();

            return temp1.compareTo(temp2);
        }
    }

    public static class DataComparator implements Comparator<Data> {
        public int compare(Data d1, Data d2) {
            return new AsComparator().compare(d1.contents.get(0), d2.contents.get(0));
        }
    }

    static String fileName = "text.txt";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File(fileName));

        StringBuilder sb = new StringBuilder();

        while (s.hasNext()) {
            sb.append(s.next() + " ");
        }

//        System.out.println(sb + "\n");

        List<Data> all = new ArrayList<>();
        List<Data> multiple = new ArrayList<>();
        List<Data> single = new ArrayList<>();

        String[] imports = sb.toString().trim().split(";");
        for (int i = 0; i < imports.length; i++) {
            String str = imports[i].trim();
//            System.out.println(str);

            int left = str.indexOf("import {");
            int right = str.indexOf("} from");

            int imp = "import {".length();
            int from = str.indexOf("from");

//            System.out.println(left + " " + right);

            boolean curly = (left != -1 && right != -1);

            int leftPos = curly ? imp : 7;
            int rightPos = curly ? right : from;

            String[] parsed = str.substring(leftPos, rightPos).split(",");

//            System.out.println(Arrays.toString(parsed));

            Data data = new Data(curly ? "import {" : "import", str.substring(curly ? right : from));

            boolean isAll = false;
            for (String parse : parsed) {
                if (parse.charAt(0) == '*') {
                    isAll = true;
//                    System.out.println("YES");
                }
                data.add(parse.trim());
            }

            data.sort();

            if (isAll) {
                all.add(data);
            }
            else if (data.contents.size() > 1) {
                multiple.add(data);
            } else {
                single.add(data);
            }
        }

        printData(all);
        printData(multiple);
        printData(single);
    }

    public static void printData (List<Data> list) {
        Collections.sort(list, new DataComparator());


        for (Data d : list) {
            System.out.print(d.left + " ");

            boolean enter = d.contents.size() >= 5;
            if (enter) {
                System.out.print("\n  ");
            }

            System.out.print(d.contents.get(0));

            for (int i = 1; i < d.contents.size(); i++) {
                System.out.print(",");
                if (enter) {
                    System.out.print("\n  ");
                } else {
                    System.out.print(" ");
                }

                System.out.print(d.contents.get(i));
            }

            if (enter) {
                System.out.print("\n");
            } else {
                System.out.print(" ");
            }
            System.out.println(d.path + ";");
        }
    }
}
