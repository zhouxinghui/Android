package com.yun.javatest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by unicho on 2017/11/8.
 */

public class SPCreate {
    private int baseSP;
    private int baseDpi;
    private String dirStr = "./res";

    private final static String WTemplate = "<dimen name=\"s{0}\">{1}sp</dimen>\n";

    /**
     * {0}-HEIGHT
     */
    private final static String VALUE_TEMPLATE = "values-{0}dpi";

    private static final String SUPPORT_DIMESION = "240,280";
    //    private static final String SUPPORT_DIMESION = "1184,720;1196,720;1280,720;1024,768;1280,800;1800,1080;1812,1080;1920,1080;1920,1116;1836,1200;1920,1200;2560,1440;2560,1600;";


    public SPCreate(int baseWDp, int baseDpi) {
        this.baseSP = baseWDp;
        this.baseDpi = baseDpi;

        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdir();

        }
        System.out.println(dir.getAbsoluteFile());

    }

    public void generate() {
        String[] vals = SUPPORT_DIMESION.split(",");
        for (String val : vals) {
            generateXmlFile(Integer.valueOf(val));
        }

    }

    private void generateXmlFile(int dpi) {

        StringBuffer sbForWidth = new StringBuffer();
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForWidth.append("<resources>");
        float cellw = baseDpi/ (dpi * 1.0f);

        for (int i = 1; i <= baseSP; i++) {
            sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sbForWidth.append("</resources>");

        File fileDir = new File(dirStr + File.separator + VALUE_TEMPLATE.replace("{0}", dpi + ""));
        fileDir.mkdir();

        File layDFile = new File(fileDir.getAbsolutePath(), "sp.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layDFile));
            pw.print(sbForWidth.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static float change(float a) {
        int temp = (int) (a * 10);
        return Math.round((float)temp/10);
    }

    public static void main(String[] args) {
        int baseSP = 50;
        int baseDpi = 240;

        new SPCreate(baseSP,baseDpi).generate();
    }
}
