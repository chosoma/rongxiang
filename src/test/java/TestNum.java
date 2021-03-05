public class TestNum {
    public static void main(String[] args) {

//        String str = "\"17044\":{\"field\":\"\",\"num\":\"\",\"table_name\":\"\",\"注释\":\"\"},";
//        System.out.println(str);
        String[] strs = {"压力", "密度", "温度"};
        String[] field = {"YALI", "MIDU", "WENDU"};
        String[] phases = {"A", "B", "C"};
        for (int i = 17044, count = 0; i < 17125; i++, count++) {
            System.out.println("\"" + (count + 1) + "\":{\"field\":\"" + field[count % 3] + "\",\"point_num\":\"" + (count / 9 + 1) + "\",\"unit_num\":\"" + (count / 3 + 1) + "\",\"phase\":\"" + phases[count % 9 / 3] + "\",\"注释\":\"" + strs[count % 3] + "\"},");
        }
        /* for (int i = 679, count = 0; i < 730; i++, count++) {
            System.out.println("\"" + i + "\":{\"point_num\":\"" + (count / 3 + 1) + "\",\"phase\":\"" + phases[count %  3] + "\",\"注释\":\"\"},");
        }*/


    }
}
